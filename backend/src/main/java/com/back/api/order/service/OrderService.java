package com.back.api.order.service;

import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.CompositeData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

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

    public Order createOrder(String email, long productId, long quantity) {
        Member member = memberRepository.findByEmail(email).get();
        Product product = productRepository.findById(productId).get();

        Order order = new Order(member);
        order.setOrderDate(LocalDate.now());

        OrderProduct orderProduct = new OrderProduct(product);
        if(quantity > 0)
            orderProduct.updateQuantity(quantity);

        order.addOrderProduct(orderProduct);

        return orderRepository.save(order);

    }

    //주문 수정(업데이트)
    @Transactional
    public Order updateOrder(String email, long productId, long quantity){

        Member member = memberRepository.findByEmail(email).get();
        Product product = productRepository.findById(productId).get();
        Order order = orderRepository.findByMember(member);
        order.updateProduct(product, quantity);



        return  orderRepository.save(order);

    }


    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
