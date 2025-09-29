package com.back.api.order.schedule;

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
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "order.schedule.cron=*/3 * * * * ?" // 이 클래스 안에서는 cron이 3초마다 실행
})
@ActiveProfiles("test")
class OrderSchedulerSuccessTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void schedulerRun() throws InterruptedException {
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

        // 3초마다 실행되므로, 4초 기다리면 최소 한 번은 주문 스케줄러가 실행됨
        Thread.sleep(4000);

        Order resultOrder = orderRepository.findByMember(member);
        assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
    }
}