package com.back.api.payment.service;

import com.back.api.order.service.OrderProductService;
import com.back.api.order.service.OrderService;
import com.back.api.payment.dto.request.PaymentCreateRequest;
import com.back.api.payment.dto.response.PaymentCreateResponse;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.payment.entity.Payment;
import com.back.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderProductService orderProductService;

    private final OrderService orderService;

    @Transactional
    public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
        Order order =  orderService.getId(request.orderId());

        orderService.validateOrderStatus(order, OrderStatus.PENDING);

        List<OrderProduct> result = orderProductService.getOrderProductByOrder(order);

        Long amount = result.stream()
                .mapToLong(op -> op.getProduct().getPrice() * op.getQuantity())
                .sum();

        Payment payment = Payment.builder()
                .paymentMethod(request.paymentMethod())
                .order(result.getFirst().getOrder())
                .amount(amount)
                .build();

        paymentRepository.save(payment);
        return PaymentCreateResponse.from(payment, order.getMember());
    }
}
