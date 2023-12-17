package com.coviscon.postservice.repository;

import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.repository.custom.CommentRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

//    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
//    Optional<Comment> findCommentByIdWithParent(@Param("id") Long commentId);
}
