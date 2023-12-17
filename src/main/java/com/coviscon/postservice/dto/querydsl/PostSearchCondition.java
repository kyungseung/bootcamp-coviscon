package com.coviscon.postservice.dto.querydsl;

import com.coviscon.postservice.entity.post.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCondition {

    private String title;
    private String content;
    private QnaStatus qnaStatus;
}
