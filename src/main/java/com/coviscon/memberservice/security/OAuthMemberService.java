package com.coviscon.memberservice.security;

import com.coviscon.memberservice.entity.member.Member;
import com.coviscon.memberservice.entity.member.Role;
import com.coviscon.memberservice.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthMemberService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[OAuthMemberService loadUser] login access");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        Optional<Member> foundMember = memberRepository.findByEmail(email);

        if (foundMember.isEmpty()) { //찾지 못했다면
            Member member = Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode("password"))
                    .nickName(email.substring(0, email.indexOf("@")))
                    .role(Role.ROLE_STUDENT)
                    .build();

            Member savedMember = memberRepository.save(member);
            setSession(savedMember);
        } else {
            Member member = foundMember.orElseThrow(RuntimeException::new);
            setSession(member);
        }

        return oAuth2User;
    }

    private void setSession(Member member) {
        session.setAttribute("memberId", String.valueOf(member.getId()));
        session.setAttribute("email", member.getEmail());
        session.setAttribute("nickName", member.getNickName());
        session.setAttribute("role", String.valueOf(member.getRole()));
    }

}