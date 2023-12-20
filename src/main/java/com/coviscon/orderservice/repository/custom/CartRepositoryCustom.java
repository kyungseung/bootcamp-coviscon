package com.coviscon.orderservice.repository.custom;

import com.coviscon.orderservice.dto.ResponseCartDetail;

import java.util.List;

public interface CartRepositoryCustom {

    List<ResponseCartDetail> checkCartItem(Long memberId);
    List<ResponseCartDetail> checkOrderItem(Long memberId);
}
