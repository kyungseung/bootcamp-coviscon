package com.coviscon.itemservice.repository.custom;


import com.coviscon.itemservice.dto.querydsl.PostSearchCondition;
import com.coviscon.itemservice.dto.response.ResponsePostList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<ResponsePostList> searchByKeyword(PostSearchCondition postSearchCondition, Long itemId, Pageable pageable);
}
