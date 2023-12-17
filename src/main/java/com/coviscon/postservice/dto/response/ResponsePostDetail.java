package com.coviscon.postservice.dto.response;

import com.coviscon.postservice.entity.item.Item;
import com.coviscon.postservice.entity.item.Lecture;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.entity.post.QnaStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponsePostDetail implements Serializable {

    /* 게시글 정보 */
    private Long qnaId;
    private LocalDateTime lastModifiedDate;
    private String title;
    private String content;
    private Long memberId;
    private String nickName;
    private QnaStatus qnaStatus;

    /* 강의 정보
        -> 강의 명, 강사 명 */
    private Long itemId;
    private String itemTitle;

    public ResponsePostDetail goPost(Qna qna) {
        this.qnaId = qna.getId();
        this.lastModifiedDate = qna.getLastModifiedDate();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.memberId = qna.getMemberId();
        this.nickName = qna.getNickName();
        this.qnaStatus = qna.getQnaStatus();

        return this;
    }

    @QueryProjection
    public ResponsePostDetail(String title, String content, QnaStatus qnaStatus) {
        this.title = title;
        this.content = content;
        this.qnaStatus = qnaStatus;
    }

    @QueryProjection
    public ResponsePostDetail(Long qnaId, LocalDateTime lastModifiedDate, String title, String content, QnaStatus qnaStatus, String nickName) {
        this.qnaId = qnaId;
        this.lastModifiedDate = lastModifiedDate;
        this.title = title;
        this.content = content;
        this.qnaStatus = qnaStatus;
        this.nickName = nickName;
    }

    @QueryProjection
    public ResponsePostDetail(Long qnaId, LocalDateTime lastModifiedDate, String title, String content, QnaStatus qnaStatus, String nickName, Long itemId) {
        this.qnaId = qnaId;
        this.lastModifiedDate = lastModifiedDate;
        this.title = title;
        this.content = content;
        this.qnaStatus = qnaStatus;
        this.nickName = nickName;
        this.itemId = itemId;
    }

}
