package com.coviscon.orderservice.service.impl;

import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.dto.ResponseCartDetail;
import com.coviscon.orderservice.entity.item.Cart;
import com.coviscon.orderservice.entity.item.Lecture;
import com.coviscon.orderservice.exception.CustomException;
import com.coviscon.orderservice.exception.ErrorCode;
import com.coviscon.orderservice.repository.CartRepository;
import com.coviscon.orderservice.repository.ItemRepository;
import com.coviscon.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    /**
     * 장바구니에 강의 담기
     * 1. memberId
     * 2. cartId 를 통해서 Item 조회
     */
    @Override
    @Transactional
    public ResponseCartDetail addCart(MemberResponseDto member, Long itemId) {
        Lecture lecture = (Lecture) itemRepository.findById(itemId).orElseThrow(RuntimeException::new);
        log.info("[CartServiceImpl] lecture : {}", lecture);

        List<Cart> foundCarts = cartRepository.findByMemberId(member.getMemberId());
        for (Cart foundCart : foundCarts) {
            if (foundCart.getItemId().equals(itemId) && foundCart.getIsOrder()) {
                throw new CustomException(ErrorCode.DUPLICATE_ORDER);
            }
            if (foundCart.getItemId().equals(itemId)) {
                throw new CustomException(ErrorCode.DUPLICATE_CART);
            }
        }

        Cart cart = Cart.createCart(member, lecture);
        log.info("[CartServiceImpl] savedCart : {}", cart);

        Cart savedCart = cartRepository.save(cart);
        log.info("[addCart : saveCart] {}", savedCart);
        return setResponseCartDetail(member.getMemberId(), savedCart, lecture);

        /**
         * dto : member 정보 + cart list
         * db 에서 cart list 조회 -> 저장
         * dto 에 member 정보 set
         */
    }

    /**
     * 장바구니 리스트 가져오기
     */
    @Override
    public List<ResponseCartDetail> cartList(Long memberId) {

        return cartRepository.checkCartItem(memberId);
    }

    /**
     * 장바구니 리스트 가져오기
     */
    @Override
    public List<ResponseCartDetail> carts(Long memberId) {
        ModelMapper mapper = new ModelMapper();

        List<Cart> cart = cartRepository.findByMemberId(memberId);

        List<ResponseCartDetail> cartList = new ArrayList<>();
        cart.forEach(
                c -> cartList.add(mapper.map(c, ResponseCartDetail.class))
        );

        return cartList;
    }

    /**
     * 장바구니 선택 목록 삭제하기
     * 1. memberId
     * 2. cartId 를 통해서 Item 조회 및 삭제
     */
    @Override
    @Transactional
    public void deleteCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    private ResponseCartDetail setResponseCartDetail(Long memberId, Cart cart, Lecture lecture) {
        ResponseCartDetail responseCartDetail = new ResponseCartDetail();
        responseCartDetail.setCartId(cart.getId());
        responseCartDetail.setItemId(cart.getItemId());
        responseCartDetail.setTitle(lecture.getTitle());
        responseCartDetail.setPrice(lecture.getPrice());
        responseCartDetail.setTeacherId(lecture.getTeacherId());
        responseCartDetail.setTeacherName(lecture.getTeacherName());
        responseCartDetail.setIsOrder(lecture.isDelete());
        responseCartDetail.setCategory(lecture.getCategory());
        responseCartDetail.setMemberId(memberId);
        responseCartDetail.setIsOrder(false);

        return responseCartDetail;
    }
}
