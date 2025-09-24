package com.back.api.order.service;

import com.back.api.order.repository.OrderRepository;
import com.back.domain.order.entity.OrderStatus;
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
        LocalDateTime start = yesterday.withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = now.withHour(14).withMinute(0).withSecond(0).withNano(0);

        // PROCESSING -> SHIPPED 처리 및 처리 완료된 주문 개수 반환
        int processComplete = orderRepository.updateStatusToShipped(OrderStatus.PROCESSING, OrderStatus.SHIPPED, start, end);

        return processComplete;
    }
}
