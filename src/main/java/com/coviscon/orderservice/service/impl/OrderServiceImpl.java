package com.coviscon.orderservice.service.impl;

import com.coviscon.orderservice.constant.Encrypt;
import com.coviscon.orderservice.dto.*;
import com.coviscon.orderservice.entity.Order;
import com.coviscon.orderservice.entity.OrderStatus;
import com.coviscon.orderservice.entity.item.Cart;
import com.coviscon.orderservice.exception.CustomException;
import com.coviscon.orderservice.exception.ErrorCode;
import com.coviscon.orderservice.repository.CartRepository;
import com.coviscon.orderservice.repository.OrderRepository;
import com.coviscon.orderservice.service.OrderService;
import com.siot.IamportRestClient.IamportClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final IamportClient iamportClient;

    public OrderServiceImpl(Environment env, OrderRepository orderRepository, CartRepository cartRepository) {
        this.iamportClient = new IamportClient(
                env.getProperty("iamport.apiKey"),
                env.getProperty("iamport.secretKey"));
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;

    }


    /**
     * order dto 들어옴
     * setter order dto 값 2개를 암호화한 값으로 대체
     * 암호화 된 카드번호 = 암호화 메서드(orderdto.get("카드번호"))
     * orderdto.set(암호화 된 카드번호)
     * -> orderRepository.save()
     */


//    private List<OrderItem> setOrderItems(PaymentDto memberItemResponseDto) {
//        List<OrderItem> orderItems = new ArrayList<>();
//
//        for (Item item : memberItemResponseDto.getItems()) {
//            OrderItem orderItem = OrderItem.orderItem(item.getTitle(), item.getPrice(),
//                    item.getTeacherName());
//            orderItems.add(orderItem);
//        }
//        log.info("orderItems : {}", orderItems);
//        return orderItems;
//
//    }

    /**
     * 결제하기 버튼눌렀을떄.....
     */
//    @Override
//    @Transactional
//    public PaymentDto orderItems(PaymentDto memberResponseDto) {
//
//
//        List<OrderItem> orderItems = setOrderItems(memberResponseDto);
//        Order order = Order.createOrder(memberResponseDto.getMemberId(), orderItems);
//
//        orderRepository.save(order);
//        log.info(" order: {}", order);
//
//        return setPaymentDto(memberResponseDto, order);
//    }

    /**
     * 결제가 완료된 경우 session 저장
     */
    @Override
    @Transactional
    public PaymentDto createPayment(MemberResponseDto member) throws Exception {
        log.info("OrderServiceImpl createOrder MemberResponseDto :{}", member);

        List<Cart> findCart = cartRepository.findByMemberId(member.getMemberId());
        String merchantUid = String.valueOf(member.getMemberId() + new Date().getTime());

        Order order = Order.createOrder(member, findCart, merchantUid);
        for (Cart cart : findCart) {
            cart.updateOrder(order);
        }

        Order savedOrder = orderRepository.save(order);

        return setPaymentDto(member, findCart, merchantUid);
    }

    /**
     * 결제가 성공하면 DB에 저장하기
     */
    @Override
    @Transactional
    public void savedOrder(PaymentDto paymentDto) throws Exception {
        log.info("[OrderServiceImpl savedOrder] PaymentDto : {}", paymentDto);

        if (paymentDto == null) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER);
        }
        /* cart 의 결제 상태 활성화 */
//
        paymentDto.getCarts().forEach(cart -> {
            cart.setIsOrder(true);
            // 다른 필요한 로직이 있다면 여기에 추가할 수 있습니다.
        });

//        Order order = Order.createOrder(paymentDto);
//        Order savedOrder = orderRepository.save(order);

//        savedOrder.getCarts().forEach(
//                cart -> cart.updateOrder(savedOrder));
    }

    /**
     * 결제 목록 가져오기
     */
    @Override
    public List<ResponseOrderList> orderList(Long memberId) throws Exception {
        List<Order> orderList = orderRepository.findByMemberId(memberId);

        return setOrderList(memberId, orderList);
    }

    /**
     * 결제 상세 내역 가져오기
     */
    @Override
    public List<ResponseOrderDetail> orderDetails(Long memberId, Long orderId) throws Exception {
        List<Order> orders = orderRepository.findByMemberIdAndId(memberId, orderId);

        return setOrderDetailList(memberId, orders);
    }

    private PaymentDto setPaymentDto(MemberResponseDto member, List<Cart> carts, String merchantUid) {
        ModelMapper mapper = new ModelMapper();

        List<ResponseCartDetail> cartDetails = new ArrayList<>();
        carts.forEach(cart -> cartDetails.add(mapper.map(cart, ResponseCartDetail.class)));

        int totalPrice = 0;
        for (Cart cart : carts) {
            totalPrice += cart.getPrice();
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setMerchantUid(merchantUid);
        paymentDto.setTotalPrice(totalPrice);
        paymentDto.setOrderCode(UUID.randomUUID().toString());
        paymentDto.setOrderStatus(String.valueOf(OrderStatus.NONE));
        paymentDto.setMemberId(member.getMemberId());
        paymentDto.setNickName(member.getNickName());
        paymentDto.setEmail(member.getEmail());
        paymentDto.setCarts(carts);
        return paymentDto;
    }

    private List<ResponseOrderList> setOrderList(Long memberId, List<Order> orders) throws Exception {
        List<ResponseOrderList> orderList = new ArrayList<>();
        for (Order order : orders) {
            ResponseOrderList responseOrder = ResponseOrderList.builder()
                    .memberId(memberId)
                    .orderId(order.getId())
                    .imp_uid(order.getImp_uid())  // 결제 번호
                    .merchantUid(Encrypt.aesCBCDecode(order.getMerchantUid()))
                    .total_price(Integer.parseInt(Encrypt.aesCBCDecode(order.getTotalPrice())))
                    .orderStatus(order.getOrderStatus())
                    .build();

            orderList.add(responseOrder);
        }

        return orderList;
    }


    private List<ResponseOrderDetail> setOrderDetailList(Long memberId, List<Order> orders) throws Exception {
        List<ResponseOrderDetail> orderDetails = new ArrayList<>();
        for (Order order : orders) {

            List<Cart> carts = order.getCarts();
            for (Cart cart : carts) {
                ResponseOrderDetail orderDetail = ResponseOrderDetail.builder()
                        .memberId(memberId)
                        .orderId(order.getId())
                        .imp_uid(order.getImp_uid())  // 결제 번호
                        .title(cart.getTitle())
                        .price(cart.getPrice())
                        .teacherName(cart.getTeacherName())
                        .totalPrice(Integer.parseInt(Encrypt.aesCBCDecode(order.getTotalPrice())))
                        .build();

                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }
}