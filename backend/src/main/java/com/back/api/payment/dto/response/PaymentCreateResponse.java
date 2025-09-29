package com.back.api.payment.dto.response;

import com.back.domain.member.entity.Member;
import com.back.domain.payment.entity.Payment;
import com.back.domain.payment.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PaymentCreateResponse", description = "결제 생성 응답 dto")
public record PaymentCreateResponse(

        @Schema(description = "결제 id", example = "1")
        Long paymentId,
        @Schema(description = "결제 금액", example = "10000")
        Long amount,
        @Schema(description = "결제 수단", example = "신용카드")
        PaymentMethod method,
        @Schema(description = "회원 이메일", example = "test@naver.com")
        String email
) {
    public static PaymentCreateResponse from(Payment payment, Member member) {
        return new PaymentCreateResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                member.getEmail()
        );
    }
}
