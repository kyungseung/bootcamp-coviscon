package com.coviscon.postservice.controller.api;

import com.coviscon.postservice.dto.request.RequestPostCreate;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
//@RequestMapping("/post-service")
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;


    /*
     *  post 작성 (로그인 정보가 있어야지 작성가능)
     */
    @PostMapping("/create")
    public ResponseEntity<ResponsePostDetail> createPost(
        @ModelAttribute RequestPostCreate requestPostCreate,
        HttpSession session) {
        ObjectMapper mapper = new ObjectMapper();
        MemberResponseDto member = mapper.convertValue(session.getAttribute("member"), MemberResponseDto.class);

        ResponsePostDetail responsePostDetail = postService.createPost(member, requestPostCreate, session);
        log.info("[PostApiController] RequestPostCreate : {}", responsePostDetail);

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


    /**
     * @Controller memberQnas
     * member (student, teacher) 를 통해 자신이 포함 된 qna List 전부 조회
     * dto 변수명 확인 필요
     * @RequestBody 확인 필요
     */
    @GetMapping("/post/list")
    public ResponseEntity<List<ResponsePostDetail>> searchAllPost(
        @RequestBody MemberResponseDto memberResponseDto) {

        List<ResponsePostDetail> postList = postService.searchAllPost(memberResponseDto);

        log.info("[PostApiController searchAllPost] searchAllPost : {}", postList);
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    /**
     * @Controller qnaModify
     * member (student), qnaId 를 통해 작성한 게시글 조회
     * dto 변수명 확인 필요
     * @RequestBody 확인 필요
     */
    @GetMapping("/post/{qnaId}/detail")
    public ResponseEntity<ResponsePostDetail> searchQna(
        @PathVariable Long qnaId,
        @RequestBody MemberResponseDto memberResponseDto) {

        ResponsePostDetail responsePostDetail = postService.searchPost(qnaId, memberResponseDto);

        log.info("[PostApiController] searchQna : {}", responsePostDetail);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDetail);
    }

    // 마이페이지를 통해서 들어가는 리스트
    @GetMapping("/{itemId}/list")
    public ResponseEntity<Page<ResponsePostDetail>> searchPostKeyword(
        @PathVariable Long itemId,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "ALL") String qnaStatus,
        Pageable pageable) {
        log.info("search: {}, keyword: {}", search, keyword);

        Page<ResponsePostDetail> qnaList = postService.searchByKeyword(search, keyword, qnaStatus, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(qnaList);
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

}