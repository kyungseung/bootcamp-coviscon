package com.coviscon.postservice.controller;

import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.dto.response.ResponseCommentList;
import com.coviscon.postservice.dto.response.ResponsePostDetail;
import com.coviscon.postservice.dto.response.ResponsePostEdit;
import com.coviscon.postservice.service.CommentService;
import com.coviscon.postservice.service.PostService;

import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
//@RequestMapping("/post-service")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /*
    *   [커뮤니티] Post 전체 리스트 + Post 질문 검색
    */
    @GetMapping("/list")
    public String searchPostKeyword(
        @RequestParam(defaultValue = "ALL") String qnaStatus,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "") String keyword,
        @PageableDefault(size = 20) Pageable pageable,
        Model model) {

        log.info("search : {}, keyword : {}", search, keyword);

        Page<ResponsePostDetail> postLists = postService.searchByKeyword(search, keyword, qnaStatus, pageable);

        int totalPage = postService.postTotalPage(search, keyword, qnaStatus, pageable);
        int startPage = 0;
        int endPage = totalPage-1;

        model.addAttribute("presentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", postLists.getTotalPages());
        model.addAttribute("pageable", pageable);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("postLists", postLists);

        return "/post/list";
    }

    /*
     *   [커뮤니티] Post 작성 페이지 이동
     */
    @GetMapping("/community/create")
    public String createCommunityPostForm() {
        return "post/create";
    }


    /*
     *   [특정 강의에 대해] Post 작성 페이지 이동
     */
    @GetMapping("/{itemId}/create")
    public String createPostForm(@PathVariable Long itemId, Model model) {
        model.addAttribute("itemId", itemId);
        return "post/create";
    }

    /*
     *   [커뮤니티] qnaId를 기준으로 게시글 상세보기
     */
    @GetMapping("/{qnaId}/detail")
    public String detailCommunityPostById(
        @PathVariable Long qnaId,
        Model model,
        HttpServletRequest request) {

        return detailPost(qnaId, model, request, null);
    }


    /*
     *   [특정 강의에 대해] qnaId, itemId를 기준으로 게시글 상세보기
     */
    @GetMapping("/{qnaId}/{itemId}/detail")
    public String detailPostById(
        @PathVariable Long qnaId,
        @PathVariable Long itemId,
        Model model,
        HttpServletRequest request) {

        return detailPost(qnaId, model, request, itemId);
    }

    /*
     *   [특정 강의 + 커뮤니티] 기존 값을 수정페이지로 넘겨줌
     */
    @GetMapping("/{qnaId}/edit")
    public String updatePost(@PathVariable Long qnaId, Model model) {
        try {
            ResponsePostEdit responsePostEdit = postService.modifyPostById(qnaId);
            log.info("[PostController updatePost] responsePostEdit {}", responsePostEdit);
            model.addAttribute("responsePostEdit", responsePostEdit);
            return "post/edit";

        } catch (NoSuchElementException e) {
            return "post/list";
        }
    }

    /*
    *   [특정 강의 + 커뮤니티] 게시글 상세보기
    */
    private String detailPost(
        Long qnaId,
        Model model,
        HttpServletRequest request,
        Long itemId) {

        HttpSession session = request.getSession();
        MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");

        log.info("[PostController detailPostById] member: {}", member);
        log.info("[PostController detailPostById] qnaId : {}", qnaId);
        if (itemId != null) {
            log.info("[PostController detailPostById] itemId : {}", itemId);
        }

        ResponsePostDetail responsePostDetail;

        // 특정 강의(itemId)의 값이 null이 아닌 경우, 특정 강의에 대한 상세페이지 보기
        if (itemId != null) {
            responsePostDetail = postService.getPostById(qnaId, itemId);
        } else {
            responsePostDetail = postService.getCommunityPostById(qnaId);
        }

        List<ResponseCommentList> comments = commentService.findCommentsByPostId(qnaId);

        log.info("detailPostById getPostById {}", responsePostDetail);
        log.info("comments in detailPostById : {}", comments);

        model.addAttribute("postDetail", responsePostDetail);
        model.addAttribute("comments", comments);

        // member가 null이 아닌 경우에만 member 정보를 담아줌
        if (member != null) {
            model.addAttribute("member", member);
        }

        return "post/detail";
    }
}
