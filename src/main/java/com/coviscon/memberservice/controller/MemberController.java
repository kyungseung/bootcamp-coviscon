package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.MemberResponseDto;
import com.coviscon.memberservice.dto.querydsl.LectureSearchCondition;
import com.coviscon.memberservice.dto.vo.MemberCart;
import com.coviscon.memberservice.dto.vo.MemberQna;
import com.coviscon.memberservice.dto.vo.TeacherLecture;
import com.coviscon.memberservice.dto.vo.TeacherQna;
import com.coviscon.memberservice.exception.CustomException;
import com.coviscon.memberservice.exception.ErrorCode;
import com.coviscon.memberservice.service.MemberService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/settings")
    public String settings() {
        return "members/settings";
    }

    /**
    * 비밀번호 변경
    */
    @PostMapping("/settings/changePw")
    public String changedPassword(
        HttpSession session,
        @RequestParam String prePassword,
        @RequestParam String nextPassword
    ) {
        String email = (String) session.getAttribute("email");

        memberService.changedPassword(email, prePassword, nextPassword);
        return "redirect:/auth/logout";

    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/settings/deleteMember")
    public String deleteAccount(HttpSession session) {
        String email = (String) session.getAttribute("email");

        memberService.deleteMember(email);
        return "redirect:/auth/logout";
    }


    @GetMapping("/findEmailAndPw")
    public String findEmailAndPw() {
        return "members/findEmailAndPw";
    }

    /**
    * 아이디(이메일) 찾기 : 닉네임
    */
    @GetMapping("/findEmail")
    public String findEmailByNickName(@RequestParam String nickname, Model model) {

        String email = memberService.searchEmailByNickName(nickname);

        model.addAttribute("email", email);

        return "members/findEmailAfter";
    }


    /**
     * 비밀번호 찾기 : 이메일, 닉네임
     */
    @GetMapping("/findPassword")
    public String findPasswordByEmailAndNickName(
        @RequestParam String email,
        @RequestParam String nickname,
        Model model) {

        String password = memberService.searchPasswordByEmailAndNickName(email, nickname);

        model.addAttribute("password", password);

        return "members/findPasswordAfter";
    }
}
