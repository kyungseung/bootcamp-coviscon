package com.coviscon.orderservice.entity.item;


import com.coviscon.orderservice.entity.OrderStatus;
import com.coviscon.orderservice.entity.auditing.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Entity
public abstract class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 강의, 책 제목

    @ColumnDefault(value = "false")
    private boolean isDelete; // item 삭제 여부

    @Enumerated(EnumType.STRING)
    private Category category;
}
