package com.coviscon.itemservice.dto.response;

import com.coviscon.itemservice.entity.post.QnaStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostList {

    private Long qnaId;
    private String title;
    private String content;
    private String nickName;
    private QnaStatus qnaStatus;
    private LocalDateTime lastModifiedDate;
    private Long memberId;

    @QueryProjection
    public ResponsePostList(String title, String content, String nickName, LocalDateTime lastModifiedDate, Long qnaId, QnaStatus qnaStatus) {
        this.title = title;
        this.content = content;
        this.nickName = nickName;
        this.lastModifiedDate = lastModifiedDate;
        this.qnaId = qnaId;
        this.qnaStatus = qnaStatus;
    }

}
