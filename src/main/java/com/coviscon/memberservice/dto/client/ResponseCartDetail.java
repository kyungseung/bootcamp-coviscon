package com.coviscon.memberservice.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCartDetail {

    private Long cartId;
    private Long itemId;
    private Long teacherId;
    private String title;
    private String content;
    private int price;

    private String teacherName;
    private String press;
    private Boolean isDelete;
    private String category;

    private Long memberId;
}
