package com.back.api.payment.dto.request;

import com.back.domain.payment.entity.PaymentMethod;

public record PaymentCreateRequest(
        Long orderId,
        PaymentMethod paymentMethod
) {
}
