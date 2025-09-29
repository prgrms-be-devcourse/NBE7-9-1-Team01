package com.back.api.order.dto.response;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "OrderStatusResponse", description = "주문 상태 응답 dto")
public record OrderStatusResponse(
        @Schema(description = "주문 id", example = "1")
        Long OrderId,
        @Schema(description = "주문 날짜", example = "2023-01-01T12:00:00")
        LocalDateTime orderDate,
        @Schema(description = "주문 상태", example = "PAID")
        OrderStatus orderStatus,
        @Schema(description = "회원 이메일", example = "test@naver.com")
        String email,
        @Schema(description = "회원 우편번호", example = "12345")
        String postcode,
        @Schema(description = "회원 주소", example = "경기도 부천시")
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
