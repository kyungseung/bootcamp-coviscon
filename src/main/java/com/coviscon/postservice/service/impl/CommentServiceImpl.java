package com.coviscon.postservice.service.impl;

import com.coviscon.postservice.dto.request.RequestCommentCreate;
import com.coviscon.postservice.dto.request.RequestCommentEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.entity.post.Comment;
import com.coviscon.postservice.entity.post.Qna;
import com.coviscon.postservice.exception.CustomException;
import com.coviscon.postservice.exception.ErrorCode;
import com.coviscon.postservice.repository.CommentRepository;
import com.coviscon.postservice.repository.PostRepository;
import com.coviscon.postservice.service.CommentService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /*
     * 특정 게시글(qnaId)의 comment 조회
     */
    @Override
    @Transactional
    public List<ResponseCommentList> findCommentsByPostId(Long qnaId) {
        postRepository.findById(qnaId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        return convertNestedStructure(commentRepository.findCommentByQnaId(qnaId));
    }

    /*
     * comment 작성
     */
    @Override
    @Transactional
    public ResponseCommentList createComment(
            MemberResponseDto member,
            RequestCommentCreate requestCommentCreate) {

        log.info("[CommentServiceImpl] MemberResponseDto {}", member);

        // 새 댓글 생성
        Comment newComment = Comment.createComment(
            member,
            requestCommentCreate.getContent(),
            findPostById(requestCommentCreate.getQnaId()),
            getParentComment(requestCommentCreate.getParentId())
        );
        log.info("[CommentServiceImpl] createComment {}", newComment.toString());

        // 댓글 저장
        Comment savedComment = commentRepository.save(newComment);

        return ResponseCommentList.convertCommentToDto(savedComment);
    }

    private Qna findPostById(Long qnaId) {
        return postRepository.findById(qnaId)
            .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private Comment getParentComment(Long parentId) {
        return parentId != null ?
            commentRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)) : null;
    }

    /**
     * comment를 중첩구조로 변환
     */
    @Override
    public List<ResponseCommentList> convertNestedStructure(List<Comment> comments) {
        List<ResponseCommentList> result = new ArrayList<>();
        Map<Long, ResponseCommentList> map = new HashMap<>();

        comments.forEach(c -> {
            ResponseCommentList dto = ResponseCommentList.convertCommentToDto(c);
            map.put(dto.getCommentId(), dto);

            if(c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(dto);
            } else {
                result.add(dto);
            }
        });

        return result;
    }

    @Override
    public ResponseCommentList modifyCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        return setResponseCommentList(comment);
    }

    @Override
    @Transactional
    public void modifyComment(Long commentId, RequestCommentEdit requestCommentEdit) {
        Comment comment = this.commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        comment.updateComment(requestCommentEdit);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getChildren().isEmpty()) { // Children이 있으면 isDeleted의 값만 true로 변경
            comment.changeIsDeleted(true);
        } else {
            // Children 없을 경우, 바로 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    @Override
    public Comment getDeletableAncestorComment(Comment comment) {
        // 현재 댓글의 parent를 구함
        Comment parent = comment.getParent();

        // parent가 있고 parent의 Children이 1이고, isDeleted가 true면
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted()) {
            return getDeletableAncestorComment(parent);     // parent를 반환
        }
        return comment;
    }

    private ResponseCommentList setResponseCommentList(Comment comment) {
        return ResponseCommentList.builder()
            .commentId(comment.getId())
            .content(comment.getContent())
            .nickName(comment.getNickName())
            .tNickName(comment.getTNickName())
            .build();
    }
}
