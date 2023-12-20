package com.coviscon.orderservice.controller.api;


import com.coviscon.orderservice.constant.Encrypt;
import com.coviscon.orderservice.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrderApiController {

    private final HttpSession session;

    @GetMapping("/encrypt/")
    public ResponseEntity<String> encrypt(HttpServletRequest request) throws Exception {

        String test = "asdf123";
        String en = Encrypt.aesCBCEncode(test);
        System.out.println("en = " + en);

        String de = Encrypt.aesCBCDecode(en);
        System.out.println("de = " + de);

        return ResponseEntity.ok("");
    }

    @GetMapping("/saved/session")
    public ResponseEntity<String> savedSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberId(102L);
        memberResponseDto.setEmail("test@test.com");
        memberResponseDto.setNickName("임제정");
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
