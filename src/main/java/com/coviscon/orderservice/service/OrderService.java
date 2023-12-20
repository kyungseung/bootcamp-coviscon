package com.coviscon.orderservice.service;

import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.dto.PaymentDto;
import com.coviscon.orderservice.dto.ResponseOrderDetail;
import com.coviscon.orderservice.dto.ResponseOrderList;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {

//    public PaymentDto orderItems(PaymentDto memberResponseDto);

    PaymentDto createPayment(MemberResponseDto member) throws Exception;

    void savedOrder(PaymentDto paymentDto) throws Exception;

//    List<ResponseOrderDetail>  orderDetails(Long memberId, Long orderId) throws Exception;

    public List<ResponseOrderDetail> orderDetails(Long memberId, Long orderId) throws Exception;

    List<ResponseOrderList> orderList(Long memberId) throws Exception;

//    List<ResponseCartDetail> checkOrderItem(MemberResponseDto member);

//    Long savedOrder(HttpSession session);
}
