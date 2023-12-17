package com.coviscon.memberservice.service;

import com.coviscon.memberservice.dto.querydsl.LectureSearchCondition;
import com.coviscon.memberservice.dto.vo.MemberCart;
import com.coviscon.memberservice.dto.vo.MemberQna;
import com.coviscon.memberservice.dto.vo.TeacherLecture;
import com.coviscon.memberservice.dto.vo.TeacherQna;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

public interface MemberService {

    MemberQna searchAllMemberQna(String email);

    MemberCart searchCart(String email);

    TeacherLecture searchTeacherLectures(String email, LectureSearchCondition condition);

    TeacherQna searchAllTeacherQna(String email);

}
