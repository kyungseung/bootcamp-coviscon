package com.coviscon.orderservice.dto;

import com.coviscon.orderservice.entity.item.Category;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
/* ResponseDto -> Json 변환 후 반환 시 null 값이 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCartDetail implements Serializable {

    // cart
    private Long cartId;

    // lecture
    private Long itemId;
    private String title;
    private int price;
    private Long teacherId;
    private String teacherName;
    private Boolean isDelete;
    private Category category;

    // member
    private Long memberId;
    private String email;
    private String nickName;

    // isOrder
    private Boolean isOrder;

    public ResponseCartDetail() {
    }

    @QueryProjection
    public ResponseCartDetail(Long cartId, Long itemId, String title, int price, Long teacherId, String teacherName, Boolean isDelete, Category category, Long memberId, String email, String nickName, Boolean isOrder) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.isDelete = isDelete;
        this.category = category;
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.isOrder = isOrder;
    }
}
