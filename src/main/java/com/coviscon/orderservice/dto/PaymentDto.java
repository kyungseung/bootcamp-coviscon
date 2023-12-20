package com.coviscon.orderservice.dto;

import com.coviscon.orderservice.entity.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
/* ResponseDto -> Json 변환 후 반환 시 null 값인 데이터는 제외 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class PaymentDto implements Serializable{

    private String merchantUid; // 결제번호 (OrderId + new Date) -> OrderId
    private int totalPrice; // 가격 : 총 가격 Order.getTotalPrice()
    private String orderCode;
    private String orderStatus;

    /* 유저정보 */
    private Long memberId;
    private String email; // 이메일 (아이디)
    private String password; // 비번
    private String nickName; // 닉네임

    @JsonIgnore
    private List<Cart> carts;

//    public PaymentDto() {
//    }
//
//    public PaymentDto(String merchantUid, int totalPrice, String orderCode, String orderStatus, String imp_uid, Long memberId, String email, String password, String nickName, List<ResponseCartDetail> carts) {
//        this.merchantUid = merchantUid;
//        this.totalPrice = totalPrice;
//        this.orderCode = orderCode;
//        this.orderStatus = orderStatus;
//        this.imp_uid = imp_uid;
//        this.memberId = memberId;
//        this.email = email;
//        this.password = password;
//        this.nickName = nickName;
//        this.carts = carts;
//    }


}
