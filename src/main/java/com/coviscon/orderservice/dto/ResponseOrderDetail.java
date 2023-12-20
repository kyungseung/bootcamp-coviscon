package com.coviscon.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/* ResponseDto -> Json 변환 후 반환 시 null 값이 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrderDetail {

    private Long orderId;

    // lecture
    private Long itemId;
    private String title;
    private int price;
    private int totalPrice;
    private Long teacherId;
    private String teacherName;
    private Boolean isDelete;
    private String category;

    // member
    private Long memberId;
    private String email;
    private String nickName;

    private String imp_uid;





}
