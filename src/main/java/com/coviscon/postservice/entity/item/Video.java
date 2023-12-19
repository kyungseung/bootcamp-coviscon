package com.coviscon.postservice.entity.item;

import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // == 생성 메서드 ==
    /** 테스트용 **/
    public static Video createVideo(Lecture lecture) {
        return Video.builder()
                .realVideoName("realVideoName")
                .subVideoName("subVideoName")
                .thumbnailFileName("testspring.png")
                .savedPath("savedPath")
                .size(1000)
                .lecture(lecture)
                .build();
    }

}
