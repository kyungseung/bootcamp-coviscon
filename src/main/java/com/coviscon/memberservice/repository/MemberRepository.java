package com.coviscon.memberservice.repository;

import com.coviscon.memberservice.entity.member.Member;
import com.coviscon.memberservice.repository.querydsl.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);
}
