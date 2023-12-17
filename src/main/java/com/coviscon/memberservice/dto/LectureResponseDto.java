package com.coviscon.memberservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LectureResponseDto implements Serializable {

    private Long id;
    private String category;
    private String title;
    private String content;
    private Boolean isDelete;
    private Integer likeCnt;
    private int price;
    private String teacherName;
    private String thumbnailFileName;
    private String realVideoName;

    @QueryProjection
    public LectureResponseDto(String title, String thumbnailFileName) {
        this.title = title;
        this.thumbnailFileName = thumbnailFileName;
    }
}
