package com.coviscon.memberservice.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
/* ResponseDto -> Json 변환 후 반환 시 null 값이 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePostDetail implements Serializable {

    private Long qnaId;
    private String title;
    private String content;
    private String qnaStatus;
}
