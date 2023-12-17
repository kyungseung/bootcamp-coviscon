package com.coviscon.postservice.repository;

import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Qna, Long>, PostRepositoryCustom {

//    @Query("select q from Qna q join fetch q.lecture l where l.id = :itemId")
//    ResponsePostDetail searchPostItem(@Param("itemId") Long itemId);
}
