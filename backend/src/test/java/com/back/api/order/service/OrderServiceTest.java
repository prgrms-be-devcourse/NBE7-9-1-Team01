package com.back.api.order.service;

import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceTest {
    @Autowired
    private  MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    // OrderService.dailyOrderProcess() 테스트를 위한 객체 생성 메서드
    void createForDailyOrderProcess(OrderStatus orderStatus, LocalDateTime orderDate) {
        String email = "test@exampl.com";
        String password = "test";
        String address = "test";
        String postcode = "test";
        Role role = Role.ROLE_USER;
        Member member = new Member(email, password, address, postcode, role);
        memberRepository.save(member);

        Order order = new Order(member, orderStatus, orderDate);

        orderRepository.save(order);
    }

    @Test
    @DisplayName("어제 14시 ~ 오늘 14시 주문 처리, PAID -> SHIPPED")
    void dailyOrderProcess() {
        // 오늘 14:00:00에 생성된 PAID 주문
        OrderStatus orderStatus = OrderStatus.PAID;
        LocalDateTime orderDate = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0);
        createForDailyOrderProcess(orderStatus, orderDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(1);
    }

    @Test
    @DisplayName("어제 14시 ~ 오늘 14시 이외 주문 처리 불가")
    void dailyOrderProcess_outOfRange() {
        // 오늘 14:00:01에 생성된 PAID 주문
        OrderStatus orderStatus = OrderStatus.PAID;
        LocalDateTime orderDate = LocalDateTime.now().withHour(14).withMinute(0).withSecond(1);
        createForDailyOrderProcess(orderStatus, orderDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(0);
    }

    @Test
    @DisplayName("PROCESSING 상태가 아닌 주문은 SHIPPED 처리 불가")
    void dailyOrderProcess_notProcessing() {
        // 오늘 14:00:00에 생성된 SHIPPED 주문
        OrderStatus orderStatus = OrderStatus.SHIPPED;
        LocalDateTime orderDate = LocalDateTime.now().withHour(14).withMinute(0).withSecond(0);
        createForDailyOrderProcess(orderStatus, orderDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(0);
    }
}
