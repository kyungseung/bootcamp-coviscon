package com.coviscon.postservice.entity.item;

import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 강의 제목
    @Column(nullable = false)
    private Integer price; // 가격
    @Column(nullable = false)
    private String thumbnailFileName; // 강의 thumbnail

    private Integer totalPrice; // 총 가격

    private Long memberId; // 장바구니 주인

    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lecture> lectures = new ArrayList<>();

}
