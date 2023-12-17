package com.coviscon.postservice.dto.request;

import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.repository.CommentRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class RequestCommentCreate {

    private Long commentId;
    private String content;
    private String nickName;
    private String tNickName;
    private Long parentId;
    private Long qnaId;
}
