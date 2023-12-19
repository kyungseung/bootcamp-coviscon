package com.coviscon.memberservice.repository.querydsl;

import com.coviscon.memberservice.dto.MemberResponseDto;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepositoryCustom {

    Optional<MemberResponseDto> searchByEmail(String email);

    String searchEmailByNickName(String nickname);

    String searchPasswordByEmailAndNickName(String email, String nickname);
}
