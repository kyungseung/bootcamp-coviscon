package com.coviscon.postservice.entity.item;

import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    @Column(nullable = false)
    private String realVideoName; // 업로드 한 real name

    @Column(nullable = false)
    private String subVideoName; // 강의 가짜 이름

    @Column(nullable = false)
    private String thumbnailFileName; // 강의 thumbnail

    @Column(nullable = false)
    private String savedPath; // 저장 경로

    private Integer size; // 파일 용량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Lecture lecture;

}
