package com.coviscon.memberservice.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.coviscon.memberservice.dto.QMemberResponseDto is a Querydsl Projection type for MemberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberResponseDto extends ConstructorExpression<MemberResponseDto> {

    private static final long serialVersionUID = 1046338511L;

    public QMemberResponseDto(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> role) {
        super(MemberResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class}, memberId, email, nickName, role);
    }

    public QMemberResponseDto(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> orderId, com.querydsl.core.types.Expression<String> role) {
        super(MemberResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class}, memberId, email, nickName, orderId, role);
    }

    public QMemberResponseDto(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> nickName, com.querydsl.core.types.Expression<String> orderId, com.querydsl.core.types.Expression<? extends com.coviscon.memberservice.entity.member.Mentor> mentor, com.querydsl.core.types.Expression<String> role) {
        super(MemberResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, com.coviscon.memberservice.entity.member.Mentor.class, String.class}, memberId, email, nickName, orderId, mentor, role);
    }

}

