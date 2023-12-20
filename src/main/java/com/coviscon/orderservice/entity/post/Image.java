package com.coviscon.orderservice.entity.post;

import com.coviscon.orderservice.entity.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column
    private String imageName; // 업로드 한 이미지 파일명

    @Column
    private String saveImageName; // uuid 이미지 파일명

    @Column
    private String imagePath; // 이미지 파일 경로

    @Column
    private String contentType; // contentType

    private Long size; // 이미지 파일 용량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

}
