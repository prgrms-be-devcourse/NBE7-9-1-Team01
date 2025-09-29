package com.back.api.order.dto;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "OrderDto", description = "주문 DTO")
public record OrderDto(
        @Schema(description = "주문 ID", example = "1")
        @NotNull(message = "주문 id값은 필수입니다.")
        Long orderId,

        @Schema(description = "멤버 email", example = "user1@naver.com")
        @NotNull(message = "email을 입력해야 합니다.")
        String memberEmail,

        @Schema(description = "주문일자", example = "2025-09-29")
        @NotNull(message = "주문일자는 필수입니다.")
        LocalDate orderDate,

        @Schema(description = "주문 상품 목록")
        @NotEmpty(message = "최소 1개 이상의 상품이 필요합니다.")
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
