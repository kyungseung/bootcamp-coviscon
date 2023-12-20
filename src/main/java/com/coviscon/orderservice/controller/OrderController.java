package com.coviscon.orderservice.controller;

import com.coviscon.orderservice.dto.*;
import com.coviscon.orderservice.dto.ResponseOrderDetail;
import com.coviscon.orderservice.exception.CustomException;
import com.coviscon.orderservice.exception.ErrorCode;
import com.coviscon.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final HttpSession session;

    /**
     * 결제를 위해서 주문을 생성하는 메서드
     */
    @GetMapping("/create/order")
    public String createOrder(Model model) throws Exception {
        MemberResponseDto member = setMemberResponseDto();

        log.info("[OrderController createOrder] member : {}", member);

        PaymentDto paymentDto = orderService.createPayment(member);
        log.info("[OrderController createOrder] PaymentDto order : {}", paymentDto);

        model.addAttribute("member", member);
        model.addAttribute("order", paymentDto);

        return "order/payment";
    }

    @GetMapping("/order/{orderId}/detail")
    public String orderDetail(Model model, @PathVariable Long orderId) throws Exception {
        log.info("[OrderController orderDetail] orderId : {}", orderId);

        MemberResponseDto member = setMemberResponseDto();

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        List<ResponseOrderDetail> orderList = orderService.orderDetails(member.getMemberId(), orderId);

        model.addAttribute("orderList", orderList);
        model.addAttribute("member", member);

        return "order/orderDetail";
    }

    @GetMapping("/contract")
    public String contract() {
        return "order/contract";
    }

    @GetMapping("/list")
    public String orderList(Model model) throws Exception {
        MemberResponseDto member = setMemberResponseDto();

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        List<ResponseOrderList> orderList = orderService.orderList(member.getMemberId());

        model.addAttribute("orderList", orderList);
        model.addAttribute("member", member);

        return "order/myPage";
    }

    /**
     * session -> set MemberResponseDto
     */
    private MemberResponseDto setMemberResponseDto() {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");
        String nickName = (String) session.getAttribute("nickName");
        String role = (String) session.getAttribute("role");

        if (memberId == null)
            return null;

        return MemberResponseDto.builder()
                .memberId(Long.valueOf(memberId))
                .email(email)
                .nickName(nickName)
                .role(role)
                .build();
    }

}
