package com.coviscon.memberservice.service.impl;

import com.coviscon.memberservice.dto.MemberRequestDto;
import com.coviscon.memberservice.dto.MemberResponseDto;
import com.coviscon.memberservice.entity.member.Member;
import com.coviscon.memberservice.exception.CustomException;
import com.coviscon.memberservice.exception.ErrorCode;
import com.coviscon.memberservice.repository.MemberRepository;
import com.coviscon.memberservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("[AuthServiceImpl loadUserByUsername] email : {}", email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("member : {}", member.getEmail());
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(String.valueOf(member.getRole()))
                .build();
    }

    @Override
    public MemberResponseDto login(MemberRequestDto memberRequestDto) {
        log.info("[AuthServiceImpl login] memberRequestDto : {}", memberRequestDto);

        ModelMapper mapper = new ModelMapper();

        /* email 틀릴 시 exception */
        Member member = memberRepository.findByEmail(memberRequestDto.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAIL));

        /* password 틀릴 시 exception */
        if (!passwordEncoder.matches(memberRequestDto.getPassword(), member.getEncryptedPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }

        setSession(member);

        return mapper.map(member, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public MemberResponseDto join(MemberRequestDto memberRequestDto) {
        /* 이미 있는 회원인지 검증 */
//        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
//            throw new CustomException(ErrorCode.VALID_EMAIL);
//        }
//
//        if (memberRepository.existsByNickName(memberRequestDto.getNickName())) {
//            throw new CustomException(ErrorCode.VALID_NICK_NAME);
//        }

        memberRequestDto.setEncryptedPassword(passwordEncoder.encode(memberRequestDto.getPassword()));
        Member member = Member.createUser(memberRequestDto);

        Member savedMember = memberRepository.save(member);

        ModelMapper mapper = new ModelMapper();
        return mapper.map(savedMember, MemberResponseDto.class);
    }

    @Override
    @Transactional
    public boolean validateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean validateNickName(String nickName) {
        return memberRepository.existsByNickName(nickName);
    }

    private void setSession(Member member) {
        session.setAttribute("memberId", String.valueOf(member.getId()));
        session.setAttribute("email", member.getEmail());
        session.setAttribute("nickName", member.getNickName());
        session.setAttribute("role", String.valueOf(member.getRole()));
    }
}
