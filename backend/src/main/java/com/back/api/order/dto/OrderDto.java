package com.back.api.order.dto;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "OrderDto", description = "주문 DTO")
public record OrderDto(
        @Schema(description = "주문 ID")
        Long orderId,

        @Schema(description = "멤버 email")
        String memberEmail,

        @Schema(description = "주문일자")
        LocalDate orderDate,

        @Schema(description = "주문 상품 목록")
        List<OrderProductDto> orderProducts
        
        @Schema(description = "주문 상품 주소")
        String orderAddress,
  
        @Schema(description = "주문 상품 우편 번호")
        String orderPostcode,
        
) {
    // Order 엔티티를 Dto로 변환하는 생성자
    public OrderDto(Order order, List<OrderProduct> orderProducts) {
        this(
                order.getId(),
                order.getMember().getEmail(),
                order.getOrderDate(),
                order.getAddress(),
                order.getPostcode(),
                orderProducts.stream()
                        .map(OrderProductDto::new)
                        .collect(Collectors.toList())
        );
    }
}
