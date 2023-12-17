package com.coviscon.postservice.entity.item;

import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DynamicInsert
@DynamicUpdate
@DiscriminatorColumn(name = "DTYPE")
@Entity
public abstract class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 강의, 책 제목

//    @Builder.Default
//    @ColumnDefault(value = "false")
    private boolean isDelete; // item 삭제 여부

    @Enumerated(EnumType.STRING)
    private Category category;
}
