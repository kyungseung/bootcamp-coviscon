package com.coviscon.orderservice.dto;

import com.coviscon.orderservice.constant.Encrypt;
import com.coviscon.orderservice.entity.OrderStatus;
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
public class ResponseOrderList {


    private Long memberId;
    private Long orderId;
    private String merchantUid;
    private OrderStatus orderStatus;
    private int total_price;

    private String imp_uid;

}
