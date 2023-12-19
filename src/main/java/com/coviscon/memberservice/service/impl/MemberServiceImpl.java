package com.coviscon.memberservice.service.impl;

import com.coviscon.memberservice.entity.member.Member;
import com.coviscon.memberservice.exception.CustomException;
import com.coviscon.memberservice.exception.ErrorCode;
import com.coviscon.memberservice.repository.MemberRepository;
import com.coviscon.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /*
     *   마이페이지
     *   - 회원정보 수정 : 비밀번호 변경
     */
    @Override
    @Transactional
    public void changedPassword(String email, String prePassword, String nextPassword) {
        log.info("[MemberServiceImpl findByEmail] email : {}", email);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (passwordEncoder.matches(prePassword, member.getEncryptedPassword())) {
            member.updatePassword(nextPassword);
            member.updateEncryptedPassword(passwordEncoder.encode(nextPassword));
            memberRepository.save(member);
        } else {
            throw new CustomException(ErrorCode.PASSWORD_FAIL);
        }
    }


    /*
     *   마이페이지
     *   - 회원 탈퇴
     */
    @Override
    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }

    /*
     *   아이디 찾기
     *   : 닉네임 입력
     */
    @Override
    public String searchEmailByNickName(String nickname) {
        return memberRepository.searchEmailByNickName(nickname);
    }

    /*
     *   비밀번호 찾기
     *  : 이메일 + 닉네임 일치
     */
    @Override
    public String searchPasswordByEmailAndNickName(String email, String nickname) {
        return memberRepository.searchPasswordByEmailAndNickName(email, nickname);
    }
}
