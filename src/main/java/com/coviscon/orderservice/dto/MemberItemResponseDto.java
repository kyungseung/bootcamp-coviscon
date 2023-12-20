package com.coviscon.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/* ResponseDto -> Json 변환 후 반환 시 null 값인 데이터는 제외 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberItemResponseDto implements Serializable {

    private List<ItemDto> items;

    // == Member
    private Long memberId;
    private String email; // 이메일 (아이디)
    private String password; // 비번
    private String nickName; // 닉네임
    private String tel;

    // == Item
//    private String title;
//    private String teacherName;

//
////    private static MemberResponseDto setPaymentDto(Long memberId, Order order) {
////
////        return MemberResponseDto.builder()
////                .merchantUid(String.valueOf(order.getId() + new Date().getTime()))
////                .memberId(order.getMemberId())
////                .email(o)
////                .name(user.getName())
////                .email(user.getEmail())
////                .tel(user.getTel())
////                .address(user.getAddress().getCity() + user.getAddress().getStreet())
////                .postcode(user.getAddress().getZipcode())
////                .price(order.getTotalPrice())
////                .items(user.getItems().stream()
////                        .map(item -> new ItemDto(
////                                            item.getTitle(), item.getPrice(), item.getQuantity()
////                                    ))
////                                    .collect(Collectors.toList()))
////                            .build();
////                }
}
