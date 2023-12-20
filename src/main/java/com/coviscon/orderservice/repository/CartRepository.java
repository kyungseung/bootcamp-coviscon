package com.coviscon.orderservice.repository;

import com.coviscon.orderservice.entity.item.Cart;
import com.coviscon.orderservice.repository.custom.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

    List<Cart> findByMemberId(Long memberId);
//    List<ResponseCartDetail> findByMemberId(Long memberId);


    List<Cart> findCartByMemberIdAndItemId(Long memberId, Long itemId);

    void deleteByMemberId(Long memberId);


    Cart findByOrderId(Long orderId);


//    @Query("select o from Order o join fetch o.carts c where c.itemId = :itemId")
//    Cart findByOrderFetch(@Param("itemId") Long)

}
