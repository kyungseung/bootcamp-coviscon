package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.MemberResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        if (session.getAttribute("member") != null) {
            MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");
            model.addAttribute("member", member);
        }

        return "home";
    }
}
