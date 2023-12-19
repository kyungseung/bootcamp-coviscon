package com.coviscon.postservice.service;

import com.coviscon.postservice.dto.querydsl.PostSearchCondition;
import com.coviscon.postservice.dto.request.RequestPostCreate;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.dto.response.ResponsePostEdit;
import com.coviscon.postservice.entity.post.QnaStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    List<ResponsePostDetail> getAllPostList();

    // qna 게시글 작성
    ResponsePostDetail createPost(String imageId,
                                  RequestPostCreate requestPostCreate, MemberResponseDto member);

    int postTotalPage(String search, String keyword, String qnaStatus, Pageable pageable);

    Page<ResponsePostDetail> searchByKeyword(String search, String keyword, String qnaStatus, Pageable pageable);

    PostSearchCondition setPostSearchCondition(String search, String keyword, QnaStatus qnaStatus);

    ResponsePostDetail getPostById(Long qnaId, Long itemId);

    ResponsePostDetail getCommunityPostById(Long qnaId);

    ResponsePostEdit modifyPostById(Long qnaId);

    void modifyPost(Long qnaId, RequestPostEdit requestPostEdit);

    void deletePost(Long qnaId);

    void qnaStatusUpdate(Long qnaId);

    List<ResponsePostDetail> searchAllPost(MemberResponseDto memberResponseDto);
}
