package com.back.api.payment.service;

import com.back.api.order.service.OrderProductService;
import com.back.api.payment.dto.request.PaymentCreateRequest;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.payment.entity.Payment;
import com.back.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderProductService orderProductService;

    public Payment createPayment(PaymentCreateRequest request) {
        List<OrderProduct> result = orderProductService.getOrderProductByOrderId(request.orderId());

        Long amount = result.stream()
                .mapToLong(op -> op.getProduct().getPrice() * op.getQuantity())
                .sum();

        Payment payment = Payment.builder()
                .paymentMethod(request.paymentMethod())
                .order(result.getFirst().getOrder())
                .amount(amount)
                .build();

        return paymentRepository.save(payment);
    }
}
