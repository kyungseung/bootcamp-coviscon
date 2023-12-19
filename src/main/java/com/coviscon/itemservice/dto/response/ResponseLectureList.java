package com.coviscon.itemservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseLectureList implements Serializable {
    private Long id;
    private String title;
    private int price;
    private Boolean isDelete;
    private String teacherName;
    private Integer likeCnt;
    private String thumbnailFileName;
    private String savedPath;

    @QueryProjection
    public ResponseLectureList(Long id, String title, int price, Boolean isDelete, String teacherName, Integer likeCnt, String thumbnailFileName, String savedPath) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.isDelete = isDelete;
        this.teacherName = teacherName;
        this.likeCnt = likeCnt;
        this.thumbnailFileName = thumbnailFileName;
        this.savedPath = savedPath;
    }
}
