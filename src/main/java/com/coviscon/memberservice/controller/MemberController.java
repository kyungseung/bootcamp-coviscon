package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.querydsl.LectureSearchCondition;
import com.coviscon.memberservice.dto.vo.MemberCart;
import com.coviscon.memberservice.dto.vo.MemberQna;
import com.coviscon.memberservice.dto.vo.TeacherLecture;
import com.coviscon.memberservice.dto.vo.TeacherQna;
import com.coviscon.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * member - 결제한 강의 list
     * order-service
     */
    @GetMapping("/orders")
    public String memberOrders(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        log.info("[MemberController memberOrders] : member order list");

        /*
        * order-service 에서 조회
        * */


        return "내가 결제한 강의 리스트";
    }

    /**
     * member - 장바구니
     * item-service
     */
    @GetMapping("/cart")
    public String memberCart(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        log.info("[MemberController memberCart] : member cart list");

        MemberCart memberCart;

        if (oAuth2User != null) {
            memberCart = memberService.searchCart(
                    (String) oAuth2User.getAttributes().get("email"));
        } else {
            memberCart = memberService.searchCart(userDetails.getUsername());
        }

        model.addAttribute("member", memberCart.getMemberResponseDto());
        model.addAttribute("cart", memberCart.getCartDetails());

        return "내 장바구니 리스트";
    }

    /**
     * member - qna list
     * qna-service
     */
    @GetMapping("/qna/list")
    public String qnaList(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        log.info("[MemberController qnaList] : member qna list");

        MemberQna memberQna;

        if (oAuth2User != null) {
            memberQna = memberService.searchAllMemberQna(
                    (String) oAuth2User.getAttributes().get("email"));
        } else {
            memberQna = memberService.searchAllMemberQna(userDetails.getUsername());
        }

        model.addAttribute("member", memberQna.getMemberResponseDto());
        model.addAttribute("qnas", memberQna.getResponsePostDetails());

        return "qna list 이동";
    }

    /**
     * teacher - 등록한 강의 list
     * item-service
     */
    @GetMapping("/teacher/lectures")
    public String teacherLectures(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute LectureSearchCondition condition,
            Model model) {
        log.info("[MemberController teacherLectures] : teacher lecture list");

        TeacherLecture teacherLecture;

        if (oAuth2User != null) {
            teacherLecture = memberService.searchTeacherLectures(
                    (String) oAuth2User.getAttributes().get("email"), condition);
        } else {
            teacherLecture = memberService.searchTeacherLectures(
                    userDetails.getUsername(), condition);
        }

        model.addAttribute("member", teacherLecture.getMemberResponseDto());
        model.addAttribute("lectures", teacherLecture.getResponseLectureDetail());

        return "선생님 업로드한 강의 리스트";
    }

    /**
     * teacher - qna list
     * teacher id -> item id -> qna id
     * item-service -> qna-service
     */
    @GetMapping("/teacher/qna/list")
    public String teacherQnaList(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        log.info("[MemberController teacherQnaList] : teacher qna list");

        TeacherQna teacherQna;

        if (oAuth2User != null) {
            teacherQna = memberService.searchAllTeacherQna(
                    (String) oAuth2User.getAttributes().get("email"));
        } else {
            teacherQna = memberService.searchAllTeacherQna(
                    userDetails.getUsername());
        }

        model.addAttribute("member", teacherQna.getMemberResponseDto());
        model.addAttribute("qnas", teacherQna.getResponsePostDetails());

        return "qna list 페이지 이동";
    }

    /**
     * 추가 할지도?
     * member - 좋아요 한 강의 리스트
     */
}
