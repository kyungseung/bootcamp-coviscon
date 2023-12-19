package com.coviscon.postservice.controller.api;

import com.coviscon.postservice.dto.request.RequestPostCreate;
import com.coviscon.postservice.dto.response.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.service.PostService;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    private final HttpSession session;

    /*
     *  post 작성 (로그인 정보가 있어야지 작성가능)
     */
    @PostMapping("/create")
    public ResponseEntity<ResponsePostDetail> createPost(
            @CookieValue(value = "imageId", required = false) Cookie cookie,
            @ModelAttribute RequestPostCreate requestPostCreate,
            HttpServletResponse response) {

        MemberResponseDto member = setMemberResponseDto();
        log.info("[PostApiController] MemberResponseDto : {}", member);

        String imageId = (cookie != null) ? cookie.getValue() : null;

        ResponsePostDetail responsePostDetail = postService
                .createPost(imageId, requestPostCreate, member);
        log.info("[PostApiController] RequestPostCreate : {}", responsePostDetail);

        /* cookie 삭제 */
        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostDetail);
    }

    /*
     *  post 수정
     */
    @PutMapping("/edit")
    public ResponseEntity<?> updatePost(
        @RequestParam Long qnaId,
        @ModelAttribute RequestPostEdit requestPostEdit) {

        log.info("[PostApiController updatePost] qnaId : {}", qnaId);
        log.info("[PostApiController updatePost] RequestPostEdit : {}", requestPostEdit);

        postService.modifyPost(qnaId, requestPostEdit);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /*
     *  post 삭제
     */
    @DeleteMapping("/{qnaId}/delete")
    public ResponseEntity<String> deleteQna(@PathVariable Long qnaId) {
        postService.deletePost(qnaId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*
     *  post의 상태를 변경
     *  게시글 첫 등록 시, INCOMPLETE
     *  -> 1개 이상의 comment가 생성되면 상태변경 버튼을 통해 COMPLETE로 변경
     */
    @PutMapping("{qnaId}/complete")
    public ResponseEntity<?> qnaStatusUpdate(@PathVariable Long qnaId){
        postService.qnaStatusUpdate(qnaId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /*
     *  member의 정보를 session에 저장
     */
    @GetMapping("/saved/session")
    public ResponseEntity<String> savedSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberId(101L);
        memberResponseDto.setEmail("test@test.com");
        memberResponseDto.setNickName("test");
        memberResponseDto.setRole("ROLE_STUDENT");

        session.setAttribute("member", memberResponseDto);
        return ResponseEntity.ok("");
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