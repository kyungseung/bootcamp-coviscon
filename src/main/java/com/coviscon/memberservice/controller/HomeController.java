package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final Environment env;

    @GetMapping(value = "/")
    public String redirectHome(HttpSession session, Model model) {

        if (session.getAttribute("member") != null) {
            MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");
            model.addAttribute("member", member);
        }

        return env.getProperty("redirect.url") + "home";
    }

    @GetMapping(value = "/home")
    public String home(HttpSession session, Model model) {

        if (session.getAttribute("member") != null) {
            MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");
            model.addAttribute("member", member);
        }

        return "home";
    }
}
