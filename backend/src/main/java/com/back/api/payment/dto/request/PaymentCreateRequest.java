package com.back.api.payment.dto.request;

import com.back.domain.payment.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "PaymentCreateRequest", description = "결제 생성 요청 dto")
public record PaymentCreateRequest(

        @Schema(description = "주문 id", example = "1")
        @NotNull(message = "주문 id값은 필수입니다.")
        Long orderId,
        @Schema(description = "결제 수단", example = "신용카드")
        @NotNull(message = "결제 수단은 필수입니다.")
        PaymentMethod paymentMethod
) {
}
