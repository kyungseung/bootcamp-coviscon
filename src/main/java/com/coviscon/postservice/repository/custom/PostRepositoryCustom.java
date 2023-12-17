package com.coviscon.postservice.repository.custom;

import com.coviscon.postservice.dto.querydsl.PostSearchCondition;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    List<ResponsePostDetail> searchPostByItemId(Long itemId, MemberResponseDto memberResponseDto);
    ResponsePostDetail searchPost(Long qnaId, MemberResponseDto memberResponseDto);

    List<ResponsePostDetail> searchAllPost(MemberResponseDto memberResponseDto);
    Page<ResponsePostDetail> searchByKeyword(PostSearchCondition postSearchCondition, Pageable pageable);
    int postTotalPage(PostSearchCondition postSearchCondition, Pageable pageable);
}
