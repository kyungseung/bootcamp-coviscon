package com.coviscon.itemservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {

    private Long memberId;
    private String email; // 이메일 (아이디)
    private String nickName; // 닉네임
    private String orderId; // UUID
    private String role; // 역할
}
