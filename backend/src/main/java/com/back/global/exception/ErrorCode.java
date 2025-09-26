package com.back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND,"주문을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND,"제품을 찾을 수 없습니다."),
    INVALID_ORDER_STATE(HttpStatus.BAD_REQUEST,"결제 상태를 확인해주세요."),
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND,"결제 내역을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
