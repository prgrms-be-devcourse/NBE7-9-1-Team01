package com.back.api.order.dto.response;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusResponse(
        Long OrderId,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        String email,
        String postcode,
        String address
) {
    public static OrderStatusResponse from(Order order){
        return new OrderStatusResponse(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                order.getMember().getEmail(),
                order.getMember().getPostcode(),
                order.getMember().getAddress()
        );
    }
}
