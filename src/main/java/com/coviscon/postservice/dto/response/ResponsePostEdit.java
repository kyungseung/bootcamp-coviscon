package com.coviscon.postservice.dto.response;

import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.entity.post.QnaStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponsePostEdit {

    private Long qnaId;
    private LocalDateTime lastModifiedDate;
    private String title;
    private String content;
    private QnaStatus qnaStatus;
}
