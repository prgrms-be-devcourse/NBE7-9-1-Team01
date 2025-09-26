package com.back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "값이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
