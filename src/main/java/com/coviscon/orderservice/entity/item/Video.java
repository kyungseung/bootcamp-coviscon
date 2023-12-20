package com.coviscon.orderservice.entity.item;

import com.coviscon.orderservice.entity.auditing.BaseTimeEntity;
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

    // == 생성 메서드 ==
    /** 테스트용 **/
//    public static Video createVideo(Lecture lecture) {
//        return Video.builder()
//                .realVideoName("realVideoName")
//                .subVideoName("subVideoName")
//                .thumbnailFileName("thumbnailFileName")
//                .savedPath("savedPath")
//                .size(1000)
//                .lecture(lecture)
//                .build();
//    }

    // == update ==
//    public void updateVideo(RequestCreateLecture requestCreateLecture) {
//        this.realVideoName = requestCreateLecture.getRealVideoName();
//        this.subVideoName = requestCreateLecture.getSubVideoName();
//        this.thumbnailFileName = requestCreateLecture.getThumbnailFileName();
//        this.savedPath = requestCreateLecture.getSavedPath();
//        this.size = requestCreateLecture.getSize();
//    }

    /** 동영상 업로드용 **/
//    public static Video createNewVideo(RequestCreateLecture requestCreateLecture, Lecture lecture) {
//        return Video.builder()
//                .realVideoName(requestCreateLecture.getRealVideoName())
//                .subVideoName(requestCreateLecture.getSubVideoName())
//                .thumbnailFileName(requestCreateLecture.getThumbnailFileName())
//                .savedPath(requestCreateLecture.getSavedPath())
//                .size(requestCreateLecture.getSize())
//                .lecture(lecture)
//                .build();
//    }
}
