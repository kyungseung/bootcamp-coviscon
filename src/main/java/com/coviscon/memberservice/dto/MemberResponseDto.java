package com.coviscon.memberservice.dto;

import com.coviscon.memberservice.entity.member.Mentor;
import com.coviscon.memberservice.entity.member.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/* ResponseDto -> Json 변환 후 반환 시 null 값인 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto implements Serializable {

    private Long memberId;
    private String email; // 이메일 (아이디)
    private String nickName; // 닉네임
    private String role; // [STUDENT, TEACHER, MENTOR, MENTEE]

    private String orderId; // UUID
    private Mentor mentor;

    @QueryProjection
    public MemberResponseDto(Long memberId, String email, String nickName, String role) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.role = role;
    }

    @QueryProjection
    public MemberResponseDto(Long memberId, String email, String nickName, String orderId, String role) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.orderId = orderId;
        this.role = role;
    }

    @QueryProjection
    public MemberResponseDto(Long memberId, String email, String nickName, String orderId, Mentor mentor, String role) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.orderId = orderId;
        this.mentor = mentor;
        this.role = role;
    }
}

