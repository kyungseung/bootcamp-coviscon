package com.coviscon.memberservice.repository.querydsl;

import com.coviscon.memberservice.dto.MemberResponseDto;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<MemberResponseDto> searchByEmail(String email);
}
