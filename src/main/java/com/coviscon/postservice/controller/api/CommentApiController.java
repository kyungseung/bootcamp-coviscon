package com.coviscon.postservice.controller.api;

import com.coviscon.postservice.dto.request.RequestCommentCreate;
import com.coviscon.postservice.dto.request.RequestCommentEdit;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.service.CommentService;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@RequestMapping("/post-service")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    /*
     * comment 작성
     */
    @PostMapping("/comment/create")
    public ResponseEntity<ResponseCommentList> createComment(
        RequestCommentCreate requestCommentCreate,
        HttpServletRequest request) {

        // user 정보를 session으로 가져옴
        HttpSession session = request.getSession();
        MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");
        log.info("[CommentApiController createComment] Member : {}", member);

        ResponseCommentList createdComment = commentService.createComment(member, requestCommentCreate);

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

    /*
     * user의 로그인 정보를 가져오기위해 session을 get해줌
     */
    @GetMapping("/get/session")
    public ResponseEntity<?> getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberResponseDto result = (MemberResponseDto) session.getAttribute("member");
        System.out.println("result = " + result);
        return ResponseEntity.ok("");
    }
}
