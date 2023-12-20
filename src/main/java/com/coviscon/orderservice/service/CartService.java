package com.coviscon.orderservice.service;


import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.dto.ResponseCartDetail;
import com.coviscon.orderservice.entity.OrderStatus;

import java.util.List;

public interface CartService {

    /* Cart 부분 */
    ResponseCartDetail addCart(MemberResponseDto member, Long itemId);

    List<ResponseCartDetail> carts(Long memberId);

    public List<ResponseCartDetail> cartList(Long memberId);

    void deleteCartItem(Long cartId);
}
