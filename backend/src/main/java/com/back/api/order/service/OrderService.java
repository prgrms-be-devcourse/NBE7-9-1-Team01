package com.back.api.order.service;

import com.back.domain.order.entity.Order;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.order.entity.OrderStatus;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public int dailyOrderProcess() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime start = yesterday.withHour(14).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(14).withMinute(0).withSecond(0);

        // PROCESSING -> SHIPPED 처리 및 처리 완료된 주문 개수 반환
        int processComplete = orderRepository.updateStatusToShipped(OrderStatus.PROCESSING, OrderStatus.SHIPPED, start, end);

        return processComplete;
    }

    public Order getId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_ORDER));
    }

    // 결제를 위한 체크 메소드
    @Transactional
    public void validateOrderStatus(Order order, OrderStatus orderStatus) {
        if(!order.getOrderStatus().equals(orderStatus))
            throw new ErrorException(ErrorCode.INVALID_ORDER_STATE);

        order.updateOrderStatus(OrderStatus.PAID);
    }
}
