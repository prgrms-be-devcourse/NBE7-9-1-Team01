package com.back.api.order.service;

import com.back.api.member.repository.MemberRepository;
import com.back.api.order.repository.OrderRepository;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    @DisplayName("어제 14시 ~ 오늘 14시 주문 처리, PROCESSING -> SHIPPED")
    void dailyOrderProcess() {
        // 테스트용 Member 생성
        String email = "test@exampl.com";
        String password = "test";
        String address = "test";
        String postcode = "test";
        Role role = Role.ROLE_USER;
        Member member = new Member(email, password, address, postcode, role);
        memberRepository.save(member);

        // 테스트용 Order 생성
        OrderStatus orderStatus = OrderStatus.PROCESSING;
        LocalDate orderDate = LocalDate.now().minusDays(1);
        Order order = new Order(member, orderStatus, orderDate);
        order.setCreateDate(LocalDateTime.now().minusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0));
        orderRepository.save(order);

        int processComplete = orderService.dailyOrderProcess();

        assertThat(processComplete).isEqualTo(1);
    }
}
