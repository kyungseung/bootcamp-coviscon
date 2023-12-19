package com.coviscon.itemservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseLecturePage {
    private Page<ResponseLectureList> lectureList;
    private int startPage;
    private int endPage;

    // Constructors, getters, and setters

    public ResponseLecturePage(Page<ResponseLectureList> lectureList, int startPage, int endPage) {
        this.lectureList = lectureList;
        this.startPage = startPage;
        this.endPage = endPage;
    }

}
