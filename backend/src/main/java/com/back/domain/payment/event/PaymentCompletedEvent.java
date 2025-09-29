package com.back.domain.payment.event;

import java.time.LocalDateTime;
import java.util.List;

public record PaymentCompletedEvent(
        Long paymentId,
        Long orderId,
        String buyerEmail,
        String paymentMethod,
        Long paidAmount,
        LocalDateTime paidAt,
        String address,
        List<OrderLine> lines
) {
    public record OrderLine(
            String productName,
            int quantity,
            long unitPrice,
            long lineAmount
    ) {}
}
