package com.coviscon.orderservice.entity.item;

import com.coviscon.orderservice.entity.Order;
import com.coviscon.orderservice.entity.OrderStatus;
import com.coviscon.orderservice.entity.auditing.BaseTimeEntity;
import com.coviscon.orderservice.dto.MemberResponseDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    // lecture
    private Long itemId;
    private String title;
    private int price;
    private Long teacherId;
    private String teacherName;
    private Boolean isDelete;
    private Category category;

    // member
    private Long memberId;
    private String email;
    private String nickName;

    // order
    private Boolean isOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Cart createCart(MemberResponseDto member, Lecture lecture) {
        return Cart.builder()
                .itemId(lecture.getId())
                .title(lecture.getTitle())
                .price(lecture.getPrice())
                .teacherName(lecture.getTeacherName())
                .isDelete(lecture.isDelete())
                .category(lecture.getCategory())
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .isOrder(false)
                .build();
    }

    public void updateOrder(Order order) {
        this.order = order;
        this.isOrder = true;
    }
}
