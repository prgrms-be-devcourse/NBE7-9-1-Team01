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
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
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
        Order order = getValidOrder(request.orderId());

        long amount = calculateTotalAmount(order);

        Payment payment = Payment.builder()
                .paymentMethod(request.paymentMethod())
                .order(order)
                .amount(amount)
                .build();

        paymentRepository.save(payment);
        return PaymentCreateResponse.from(payment, order.getMember());
    }

    private Order getValidOrder(Long orderId) {
        Order order =  orderService.getId(orderId);
        orderService.validateOrderStatus(order, OrderStatus.PENDING);
        return order;
    }

    private long calculateTotalAmount(Order order) {
        List<OrderProduct> orderProducts = orderProductService.getOrderProductByOrder(order);
        return orderProducts.stream()
                .mapToLong(op -> op.getProduct().getPrice() * op.getQuantity())
                .sum();
    }

    public void cancelPayment(Long id) {
        Payment payment = getPayment(id);
        Order order = payment.getOrder();
        orderService.validateOrderStatus(order, OrderStatus.PAID);
        order.updateOrderStatus(OrderStatus.CANCELED);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_PAYMENT));
    }
}
