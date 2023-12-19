package com.coviscon.itemservice.repository;

import com.coviscon.itemservice.entity.post.Qna;
import com.coviscon.itemservice.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Qna, Long>, PostRepositoryCustom {

}
