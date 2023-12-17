package com.coviscon.postservice.repository.custom;

import com.coviscon.postservice.entity.post.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface CommentRepositoryCustom {

    List<Comment> findCommentByQnaId(Long qnaId);

    Optional<Comment> findCommentByIdWithParent(Long commentId);
}
