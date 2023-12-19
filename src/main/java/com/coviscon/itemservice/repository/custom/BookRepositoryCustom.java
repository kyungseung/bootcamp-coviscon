package com.coviscon.itemservice.repository.custom;

import com.coviscon.itemservice.dto.querydsl.BookSearchCondition;
import com.coviscon.itemservice.dto.response.ResponseBookList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<ResponseBookList> searchByCondition(BookSearchCondition BookSearchCondition, Pageable pageable);
    int bookTotalPage(BookSearchCondition bookSearchCondition, Pageable pageable);
}
