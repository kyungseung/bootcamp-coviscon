package com.coviscon.memberservice.dto.querydsl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureSearchCondition {

    private String title;
    private String content;
}
