package com.back.api.order.scheduler;

import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderSchedulerFailTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;

    // cron이 없으므로, 주문 스케줄러 실행 불가
    @Test
    void schedulerRun() {
        String email = "test@exampl.com";
        String password = "test";
        String address = "test";
        String postcode = "test";
        Role role = Role.ROLE_USER;
        Member member = new Member(email, password, address, postcode, role);
        memberRepository.save(member);

        OrderStatus orderStatus = OrderStatus.PAID;
        LocalDateTime orderDate = LocalDateTime.now().withHour(13).withMinute(0).withSecond(0);
        Order order = new Order(member, orderStatus, orderDate);
        orderRepository.save(order);

        Order resultOrder = orderRepository.findByMember(member);
        assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }
}
