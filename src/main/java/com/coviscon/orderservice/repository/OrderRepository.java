package com.coviscon.orderservice.repository;

import com.coviscon.orderservice.dto.MemberItemResponseDto;
import com.coviscon.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByMemberId(Long memberId);


    List<Order> findByMemberIdAndId(Long memberId, Long orderId);
}
