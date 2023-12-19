package com.coviscon.itemservice.controller;

import com.coviscon.itemservice.dto.request.RequestCreateLecture;
import com.coviscon.itemservice.dto.response.*;
import com.coviscon.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;
    private final HttpSession session;

    /** 강의 목록 페이지 이동 **/
    @GetMapping("/lecture/list")
    public String findAllLecture (@RequestParam(defaultValue = "NONE") String category,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "") String keyword,
                                  @PageableDefault(size = 6) Pageable pageable,
                                  Model model) {
        log.info("[ItemController findAllLecture] category : {}, search : {}, keywork : {}",
                category, search, keyword);

        Page<ResponseLectureList> lectureList = itemService.searchLectureList(category, search, keyword, pageable);

        log.info("[ItemController findAllLecture] lectureList 페이징 정보 : {}", lectureList.getTotalPages());
        int totalPage = itemService.lectureTotalPage(category, search, keyword, pageable);

        int startPage = 0;
        int endPage = totalPage-1;

        model.addAttribute("totalPages", lectureList.getTotalPages());
        model.addAttribute("items", lectureList.getContent());
        model.addAttribute("pageable", pageable);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lectureList", lectureList);
        model.addAttribute("presentPage", pageable.getPageNumber());

        return "item/lectureList";
    }

    /** 책 목록 사이트 이동 **/
    @GetMapping("/book/list")
    public String findAllBook (
            @RequestParam(defaultValue = "NONE") String category,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String keyword,
            @PageableDefault(size = 6) Pageable pageable,
            Model model) {
        log.info("[ItemController findAllBook] category : {}, search : {}, keyword : {}",
                category, search, keyword);

        Page<ResponseBookList> bookList = itemService.searchBookList(category, search, keyword, pageable);
        log.info("[ItemController findAllBook] bookList : {}", bookList.getContent());
        int totalPage = itemService.bookTotalPage(category, search, keyword, pageable);

        int startPage = 0;
        int endPage = totalPage-1;

        model.addAttribute("totalPages", bookList.getTotalPages());
        model.addAttribute("books", bookList.getContent());
        model.addAttribute("pageable", pageable);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("bookList", bookList);
        model.addAttribute("presentPage", pageable.getPageNumber());

        return "item/bookList";
    }

    @GetMapping("/lecture/{itemId}/detail")
    public String goLectureDetail (@PathVariable Long itemId,
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestParam(defaultValue = "") String keyword,
                                   Pageable pageable,
                                   Model model) {
        log.info("[ItemController goLectureDetail] itemId : {}, search : {}, keyword : {}",
                itemId, search, keyword);


        ResponseLectureDetail lectureDetail = itemService.findLectureDetail(itemId);
        Page<ResponsePostList> postLists = itemService.searchPostKeyword(search, keyword, itemId, pageable);
        
        model.addAttribute("itemDetail", lectureDetail);
        model.addAttribute("postLists", postLists);

        log.info(postLists.toString());

        return "item/lectureDetail";
    }

    /** 강사 전용 강의 상세 페이지 이동 **/
    @GetMapping("/teacher/lecture/{itemId}/detail")
    public String goTLectureDetail (@PathVariable Long itemId,
                                    @RequestParam(defaultValue = "") String search,
                                    @RequestParam(defaultValue = "") String keyword,
                                    Pageable pageable,
                                    Model model) {
        log.info("[ItemController goTLectureDetail] itemId : {}, search : {}, keyword : {}",
                itemId, search, keyword);

        ResponseLectureDetail lectureDetail = itemService.findLectureDetail(itemId);
        Page<ResponsePostList> postLists = itemService.searchPostKeyword(search, keyword, itemId, pageable);

        model.addAttribute("itemDetail", lectureDetail);
        model.addAttribute("postLists", postLists);

        return "item/tLectureDetail";
    }


    /** 강사 전용 강의 목록 페이지 이동 **/
    @GetMapping("/teacher/lecture/list")
    public String findTeacherLecture (@RequestParam(defaultValue = "") String search,
                                      @RequestParam(defaultValue = "") String keyword,
                                      Model model) {
        log.info("[ItemController findTeacherLecture] search : {}, keyword : {}", search, keyword);

        MemberResponseDto member = setMemberResponseDto();

        List<ResponseLectureList> lectureList = itemService.searchTeacherLectures(member, search, keyword);

        model.addAttribute("lectureList", lectureList);

        if(member != null && member.getRole().equals("ROLE_TEACHER")) {
            return "item/teacherList";
        } else
            return "redirect:/item-service/lecture/list";
    }

    /** 유저용 강의 상세 페이지 **/
    @GetMapping("/user/lecture/{itemId}/detail")
    public String goULectureDetail (
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String keyword,
            Pageable pageable,
            Model model) {
        log.info("[ItemController goULectureDetail] search : {}, keyword : {}", search, keyword);

        MemberResponseDto member = setMemberResponseDto();

        ResponseLectureDetail lectureDetail = itemService.findLectureDetail(itemId);
        Page<ResponsePostList> postLists = itemService.searchPostKeyword(search, keyword, itemId, pageable);

        model.addAttribute("itemDetail", lectureDetail);
        model.addAttribute("postLists", postLists);

        log.info("[ItemController goULectureDetail] postList : {}", postLists.toString());

        return "item/uLectureDetail";
    }

    /** 강의 등록용 페이지 이동 **/
    @GetMapping("/lecture/save")
    public String tusUploadPage(Model model) {
        log.info("[ItemController tusUploadPage]");

        MemberResponseDto member = setMemberResponseDto();
        model.addAttribute("member", member);


        if(member != null) {
            if(member.getRole().equals("ROLE_TEACHER")) {
                return "item/create";
            }
        }
        return "";
//        return "redirect:/item-service/lecture/list"; // 아 포기
    }

    /** 강의 수정용 페이지 이동 **/
    @GetMapping("/lecture/{itemId}/modify")
    public String lectureUpdatePage(@PathVariable Long itemId, Model model) {
        log.info("[ItemController lectureUpdatePage] itemId : {}", itemId);

        MemberResponseDto member = setMemberResponseDto();
        ResponseLectureDetail lectureDetail = itemService.findLectureDetail(itemId);

        model.addAttribute("member", member);
        model.addAttribute("lectureDetail", lectureDetail);

        if(member != null && member.getRole().equals("ROLE_TEACHER")) {
            return "item/update";
        } else
            return "redirect:/item-service/lecture/list";
    }

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
