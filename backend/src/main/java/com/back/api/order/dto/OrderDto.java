package com.back.api.order.dto;

import com.back.api.order.dto.OrderProductDto;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record OrderDto(
        Long orderId,
        String memberEmail,
        LocalDate orderDate,
        List<OrderProductDto> orderProducts
) {
    // Order 엔티티를 Dto로 변환하는 생성자
    public OrderDto(Order order, List<OrderProduct> orderProducts) {
        this(
                order.getId(),
                order.getMember().getEmail(),
                order.getOrderDate(),
                orderProducts.stream()
                        .map(OrderProductDto::new)
                        .collect(Collectors.toList())
        );
    }

}
