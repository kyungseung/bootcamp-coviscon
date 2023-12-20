package com.coviscon.orderservice.repository.custom;

import com.coviscon.orderservice.dto.QResponseCartDetail;
import com.coviscon.orderservice.dto.ResponseCartDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

import static com.coviscon.orderservice.entity.item.QCart.cart;

@Slf4j
public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public CartRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ResponseCartDetail> checkCartItem(Long memberId) {
        List<ResponseCartDetail> carts = queryFactory
                .select(new QResponseCartDetail(
                        cart.id,
                        cart.itemId,
                        cart.title,
                        cart.price,
                        cart.teacherId,
                        cart.teacherName,
                        cart.isDelete,
                        cart.category,
                        cart.memberId,
                        cart.email,
                        cart.nickName,
                        cart.isOrder
                ))
                .from(cart)
                .where(
                        cart.memberId.eq(memberId),
                        cart.isOrder.eq(false)
                )
                .fetch();
        log.info("cart : {}", carts);

        return carts;
    }

    @Override
    public List<ResponseCartDetail> checkOrderItem(Long memberId) {
        List<ResponseCartDetail> carts = queryFactory
                .select(new QResponseCartDetail(
                        cart.id,
                        cart.itemId,
                        cart.title,
                        cart.price,
                        cart.teacherId,
                        cart.teacherName,
                        cart.isDelete,
                        cart.category,
                        cart.memberId,
                        cart.email,
                        cart.nickName,
                        cart.isOrder
                ))
                .from(cart)
                .where(
                        cart.memberId.eq(memberId),
                        cart.isOrder.eq(true)
                )
                .fetch();
        log.info("cart : {}", carts);

        return carts;
    }
}
