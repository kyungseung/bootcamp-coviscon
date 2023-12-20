package com.coviscon.orderservice.entity;

import com.coviscon.orderservice.constant.Encrypt;
import com.coviscon.orderservice.dto.MemberResponseDto;
import com.coviscon.orderservice.entity.auditing.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "orders_id")
    private Long id;

//    @Column(nullable = false)
    private String totalPrice; // 수량 * 단가

    private String price;

    private String title;

    private String teacherName;

    private String imp_uid; // 아임포트 주문번호

    @Column(nullable = false)
    private Long memberId; // 주문자

    @Column(nullable = false)
    private String nickname; // 주문한 사람 이름

    @Column(nullable = false)
    private String merchantUid;

    @Column(nullable = false, unique = true)
    private String orderCode; // UUID, 주문 ID

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // [CANCEL, ORDER]

    @OneToMany(mappedBy = "order")
    private List<Cart> carts = new ArrayList<>();

    // == 생성 메서드 ==
    public static Order createOrder(MemberResponseDto member, List<Cart> cart, String merchantUid) throws Exception {
        int total = 0;
        for (Cart c : cart) {
            total += c.getPrice();
        }

        return Order.builder()
                .merchantUid(Encrypt.aesCBCEncode(merchantUid))
                .totalPrice(Encrypt.aesCBCEncode(String.valueOf(total)))
                .orderCode(UUID.randomUUID().toString())
                .orderStatus(OrderStatus.ORDERS)
                .memberId(member.getMemberId())
                .nickname(Encrypt.aesCBCEncode(member.getNickName()))
                .carts(cart)
                .build();
    }

//    public static Order createOrder(PaymentDto paymentDto) throws Exception {
//        return Order.builder()
//                .merchantUid(Encrypt.aesCBCEncode(paymentDto.getMerchantUid()))
//                .totalPrice(Encrypt.aesCBCEncode(String.valueOf(paymentDto.getTotalPrice())))
//                .orderCode(paymentDto.getOrderCode())
//                .orderStatus(OrderStatus.ORDERS)
//                .memberId(paymentDto.getMemberId())
//                .nickname(Encrypt.aesCBCEncode(paymentDto.getNickName()))
//                .carts(paymentDto.getCarts())
//                .build();
//    }

    // == 비즈니스 로직 ==
    // 멤버변수 (엔티티 필드) 의 값을 바꾸는 로직
    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL_ORDER;
    }
}
