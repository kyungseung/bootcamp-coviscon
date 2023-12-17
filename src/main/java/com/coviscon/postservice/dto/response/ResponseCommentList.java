package com.coviscon.postservice.dto.response;

import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.entity.post.Qna;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseCommentList {

    private Long commentId;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private String content;
    private String nickName;
    private String tNickName;
    private List<ResponseCommentList> children = new ArrayList<>();

    public ResponseCommentList(Long id, LocalDateTime createDate, LocalDateTime lastModifiedDate,
        String content, String nickName, String tNickName) {
        this.commentId = id;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
        this.content = content;
        this.nickName = nickName;
        this.tNickName = tNickName;
    }

    public static ResponseCommentList convertCommentToDto(Comment comment) {
        return comment.getIsDeleted() ?
            new ResponseCommentList(comment.getId(), comment.getCreateDate(),
                comment.getLastModifiedDate(), "삭제된 댓글입니다.", null, null) :
            new ResponseCommentList(comment.getId(), comment.getCreateDate(),
                comment.getLastModifiedDate(), comment.getContent(), comment.getNickName(),
                comment.getTNickName());
    }
}
