package com.back.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("주문 대기"),
    PAID("결제 완료"),
    PROCESSING("상품 준비 중"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소"),
    RETURNED("반품");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
