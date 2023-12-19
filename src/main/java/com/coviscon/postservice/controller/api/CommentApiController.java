package com.coviscon.postservice.controller.api;

import com.coviscon.postservice.dto.request.RequestCommentCreate;
import com.coviscon.postservice.dto.request.RequestCommentEdit;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.service.CommentService;
import java.net.URI;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final HttpSession session;

    /*
     * comment 작성
     */
    @PostMapping("/comment/create")
    public ResponseEntity<ResponseCommentList> createComment(
        RequestCommentCreate requestCommentCreate) {

        commentService.createComment(setMemberResponseDto(), requestCommentCreate);

        // comment 작성 후, redirect를 위해 header로 넘겨줌
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/post-service/list"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /*
     * comment 수정
     */
    @PutMapping("/comment/{commentId}/edit")
    public ResponseEntity<?> updateComment(
        @PathVariable Long commentId,
        @RequestBody RequestCommentEdit requestCommentEdit) {

        commentService.modifyComment(commentId, requestCommentEdit);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*
     * comment 삭제
     */
    @DeleteMapping("/comment/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);

            return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * session -> set MemberResponseDto
     */
    private MemberResponseDto setMemberResponseDto() {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");
        String nickName = (String) session.getAttribute("nickName");
        String role = (String) session.getAttribute("role");

        if (memberId == null)
            return null;

        return MemberResponseDto.builder()
                .memberId(Long.valueOf(memberId))
                .email(email)
                .nickName(nickName)
                .role(role)
                .build();
    }
}
