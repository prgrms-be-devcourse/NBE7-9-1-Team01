package com.back.api.order.scheduler;

import com.back.api.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;

    // 매일 14:00에 주문 배송 처리
    @Scheduled(cron = "0 0 14 * * ?")
    public void dailyOrderSchedule() {
        orderService.dailyOrderProcess();
    }
}
