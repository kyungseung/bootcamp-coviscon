package com.coviscon.postservice.dto.request;

import com.coviscon.postservice.entity.post.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostCreate {

    private Long qnaId;
    private String title;
    private String content;
    private Long memberId;
    private String nickName;
    private QnaStatus qnaStatus;
    private Long itemId;

}
