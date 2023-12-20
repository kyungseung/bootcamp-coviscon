package com.coviscon.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_CART(CONFLICT, "해당 강의가 카트에 존재합니다.", "notFound"),
    DUPLICATE_ORDER(CONFLICT, "해당 주문이 이미 존재합니다.", "notFound"),


    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "로그인 후 이용하시기 바랍니다.", "notFound");


    private final HttpStatus httpStatus;
    private final String message;
    private final String page;

}
