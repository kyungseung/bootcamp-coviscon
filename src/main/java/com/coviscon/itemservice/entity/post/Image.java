package com.coviscon.itemservice.entity.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.coviscon.itemservice.entity.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
