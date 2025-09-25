package com.back.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD("신용카드"),
    BANK_TRANSFER("계좌이체"),
    POINTS("포인트");

    private final String methodName;

    PaymentMethod(String methodName) {
        this.methodName = methodName;
    }
}
