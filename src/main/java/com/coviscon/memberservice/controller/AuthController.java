package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.MemberRequestDto;
import com.coviscon.memberservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Environment env;

    /**
     * form login
     */
    @PostMapping("/login")
    public String formLogin(
            @ModelAttribute MemberRequestDto memberRequestDto) {
        log.info("[AuthController formLogin] MemberRequestDto : {}", memberRequestDto);
        authService.login(memberRequestDto);

        return env.getProperty("redirect.url");
    }

    /**
     * 회원 가입 페이지 이동
     */
    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute MemberRequestDto memberRequestDto) {
        log.info("[AuthController POST join] MemberRequestDto : {}", memberRequestDto);

        // 회원 가입
        authService.join(memberRequestDto);
        return env.getProperty("redirect.url");
    }

    /**
     * logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return env.getProperty("redirect.url");
    }
}
