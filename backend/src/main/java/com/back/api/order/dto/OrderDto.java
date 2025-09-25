package com.back.api.order.dto;

import com.back.api.order.dto.OrderProductDto;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record OrderDto(
        Long orderId,                // 주문 ID
        String memberEmail,          // 주문자 이메일
        LocalDate orderDate,         // 주문일
        List<OrderProductDto> orderProducts  // 주문 상품 목록
) {
    // Order 엔티티를 DTO로 변환하는 생성자
    public OrderDto(Order order) {
        this(
                order.getId(),
                order.getMember().getEmail(),
                order.getOrderDate(),
                order.getOrders().stream()
                        .map(OrderProductDto::new)
                        .collect(Collectors.toList())
        );
    }
}
