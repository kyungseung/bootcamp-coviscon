package com.coviscon.postservice.service;

import com.coviscon.postservice.dto.request.RequestCommentCreate;
import com.coviscon.postservice.dto.request.RequestCommentEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.entity.post.Comment;
import java.util.List;

public interface CommentService {

    List<ResponseCommentList> findCommentsByPostId(Long qnaId);

    ResponseCommentList createComment(MemberResponseDto member, RequestCommentCreate requestCommentCreate);

    List<ResponseCommentList> convertNestedStructure(List<Comment> comments);

    ResponseCommentList modifyCommentById(Long commentId);
    void modifyComment(Long commentId, RequestCommentEdit requestCommentEdit);

    void deleteComment(Long commentId);
    Comment getDeletableAncestorComment(Comment comment);
}
