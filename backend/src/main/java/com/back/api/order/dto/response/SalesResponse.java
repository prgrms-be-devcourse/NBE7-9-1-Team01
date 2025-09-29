package com.back.api.order.dto.response;

public record SalesResponse(
    String productName,
    Long totalQuantity
) {
}
