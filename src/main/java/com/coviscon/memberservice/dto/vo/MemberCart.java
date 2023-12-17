package com.coviscon.memberservice.dto.vo;

import com.coviscon.memberservice.dto.MemberResponseDto;
import com.coviscon.memberservice.dto.client.ResponseCartDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/* ResponseDto -> Json 변환 후 반환 시 null 값인 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberCart {

    private MemberResponseDto memberResponseDto;
    private List<ResponseCartDetail> cartDetails;
}
