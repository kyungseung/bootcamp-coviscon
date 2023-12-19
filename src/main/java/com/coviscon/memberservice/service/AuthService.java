package com.coviscon.memberservice.service;

import com.coviscon.memberservice.dto.MemberRequestDto;
import com.coviscon.memberservice.dto.MemberResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    MemberResponseDto login(MemberRequestDto memberRequestDto);
    MemberResponseDto join(MemberRequestDto memberRequestDto);

    boolean validateEmail(String email);
    boolean validateNickName(String nickName);
}
