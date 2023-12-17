package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.entity.member.Member;
import com.coviscon.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @GetMapping("/test")
    public ResponseEntity<Member> test(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("oAuth2User.getName() = " + oAuth2User.getName());
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oAuth2User.getAttributes().get(\"email\") = " + oAuth2User.getAttributes().get("email"));
        System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());

        String email = (String) oAuth2User.getAttributes().get("email");
        Member member = memberRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(member);
    }

    @GetMapping("/test2")
    public ResponseEntity<UserDetails> test2(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        System.out.println("userDetails.getAuthorities() = " + userDetails.getAuthorities());
        System.out.println("userDetails.getAuthorities().toString() = " + userDetails.getAuthorities().toString());

        return ResponseEntity.ok(userDetails);
    }

//    @GetMapping("/redis")
//    public ResponseEntity<MemberResponseDto> testtest(HttpSession session) {
//        MemberResponseDto member = (MemberResponseDto) session.getAttribute("member");
//        System.out.println("member = " + member);
//        return ResponseEntity.ok(member);
//    }

    @GetMapping("/redis")
    public ResponseEntity<?> getSession() {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");
        String nickName = (String) session.getAttribute("nickName");
        String role = (String) session.getAttribute("role");

        System.out.println("memberId = " + memberId);
        System.out.println("email = " + email);
        System.out.println("nickName = " + nickName);
        System.out.println("role = " + role);

        return ResponseEntity.status(HttpStatus.OK).body(memberId);
    }

    @GetMapping("/session")
    public String login(HttpSession session){
        return session.getId();
    }

    @GetMapping("/test3")
    public ResponseEntity<?> test3(
            @AuthenticationPrincipal UserDetails userDetails,
            @AuthenticationPrincipal OAuth2User oAuth2User

    ) {
        if (oAuth2User != null) {
            System.out.println("oAuth2User.getName() = " + oAuth2User.getName());
            System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
            System.out.println("oAuth2User.getAttributes().get(\"email\") = " + oAuth2User.getAttributes().get("email"));
            System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());
        }

        if (userDetails != null) {
            System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
            System.out.println("userDetails.getAuthorities() = " + userDetails.getAuthorities());
            System.out.println("userDetails.getAuthorities().toString() = " + userDetails.getAuthorities().toString());
        }

        return ResponseEntity.ok(userDetails);
    }
}
