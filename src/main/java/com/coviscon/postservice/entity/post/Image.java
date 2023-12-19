package com.coviscon.postservice.entity.post;

import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import java.nio.file.Path;
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
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    // == 생성 ==
    public static Image createImage(
        String saveImageName,
        MultipartFile multipartFile,
        Path rootLocation) {

        return Image.builder()
            .imageName(multipartFile.getOriginalFilename())
            .saveImageName(saveImageName)
            .contentType(multipartFile.getContentType())
            .size(multipartFile.getSize())
            .imagePath(rootLocation.toString() + '/' + saveImageName)
            .build();
    }
}
