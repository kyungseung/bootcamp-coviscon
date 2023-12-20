package com.coviscon.orderservice.entity.item;

import com.coviscon.orderservice.entity.post.Qna;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@DiscriminatorValue(value = "lecture")
@Entity
public class Lecture extends Item {

    @Column(length = 2000)
    private String content; // 강의 소개

    private int price; // 강의 가격
    private Long teacherId;
    private String teacherName;

    @ColumnDefault(value = "0")
    private Long buyerCnt; // 누적 학생 수

    @Setter
    @ColumnDefault(value = "0")
    private Integer likeCnt; // 좋아요 수

//    private String orderCode; // UUID


    @Builder.Default
    @OneToMany(mappedBy = "lecture", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Video> videos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "lecture")
    private List<Qna> qnas = new ArrayList<>();
}