package com.back.api.payment.dto.response;

import com.back.domain.member.entity.Member;
import com.back.domain.payment.entity.Payment;
import com.back.domain.payment.entity.PaymentMethod;

public record PaymentCreateResponse(
        Long paymentId,
        Long amount,
        PaymentMethod method,
        String email,
        String postcode,
        String address
) {
    public static PaymentCreateResponse from(Payment payment, Member member) {
        return new PaymentCreateResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                member.getEmail(),
                member.getPostcode(),
                member.getAddress()
        );
    }
}
