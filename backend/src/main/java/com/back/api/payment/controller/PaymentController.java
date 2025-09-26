package com.back.api.payment.controller;

import com.back.api.payment.dto.request.PaymentCreateRequest;
import com.back.api.payment.dto.response.PaymentCreateResponse;
import com.back.api.payment.service.PaymentService;
import com.back.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "3. [결제]", description = "결제 관련 API입니다.")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "결제 API", description = "결제를 생성합니다.")
    public ApiResponse<PaymentCreateResponse> createPayment(@RequestBody PaymentCreateRequest request) {
        PaymentCreateResponse response = paymentService.createPayment(request);
        return ApiResponse.ok("결제가 완료되었습니다.",response);
    }

}
