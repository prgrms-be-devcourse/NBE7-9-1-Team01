package com.back.api.order.service;

import com.back.api.member.repository.MemberRepository;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    // OrderService.dailyOrderProcess() 테스트를 위한 객체 생성 메서드
    void createForDailyOrderProcess(OrderStatus orderStatus, LocalDate orderDate, LocalDateTime createDate) {
        String email = "test@exampl.com";
        String password = "test";
        String address = "test";
        String postcode = "test";
        Role role = Role.ROLE_USER;
        Member member = new Member(email, password, address, postcode, role);
        memberRepository.save(member);

        Order order = new Order(member, orderStatus, orderDate);
        ReflectionTestUtils.setField(order, "createDate", createDate);
        orderRepository.save(order);
    }

    @Test
    @DisplayName("어제 14시 ~ 오늘 14시 주문 처리, PROCESSING -> SHIPPED")
    void dailyOrderProcess() {
        // 어제 15:00:00:00에 생성된 PROCESSING 주문
        OrderStatus orderStatus = OrderStatus.PROCESSING;
        LocalDate orderDate = LocalDate.now().minusDays(1);
        LocalDateTime createDate = LocalDateTime.now().minusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0);
        createForDailyOrderProcess(orderStatus, orderDate, createDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(1);
    }

    @Test
    @DisplayName("어제 14시 ~ 오늘 14시 이외 주문 처리 불가")
    void dailyOrderProcess_outOfRange() {
        // 오늘 15:00:00:00에 생성된 PROCESSING 주문
        OrderStatus orderStatus = OrderStatus.PROCESSING;
        LocalDate orderDate = LocalDate.now();
        LocalDateTime createDate = LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0);
        createForDailyOrderProcess(orderStatus, orderDate, createDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(0);
    }

    @Test
    @DisplayName("PROCESSING 상태가 아닌 주문은 SHIPPED 처리 불가")
    void dailyOrderProcess_notProcessing() {
        // 어제 15:00:00:00에 생성된 SHIPPED 주문
        OrderStatus orderStatus = OrderStatus.SHIPPED;
        LocalDate orderDate = LocalDate.now().minusDays(1);
        LocalDateTime createDate = LocalDateTime.now().minusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0);
        createForDailyOrderProcess(orderStatus, orderDate, createDate);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(0);
    }
}
