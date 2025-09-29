package com.back.api.order.dto;

import com.back.domain.order.entity.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 주문 상품 정보를 담는 DTO
 */
@Schema(name = "OrderProductDto", description = "주문 상품 DTO")
public record OrderProductDto(
        @Schema(description = "상품 ID")
        Long productId,

        @Schema(description = "상품 이름")
        String productName,

        @Schema(description = "주문 수량")
        Long quantity
) {
    // OrderProduct 엔티티를 DTO로 변환하는 생성자
    public OrderProductDto(OrderProduct orderProduct) {
        this(
                orderProduct.getProduct().getId(),
                orderProduct.getProduct().getName(),
                orderProduct.getQuantity()
        );
    }
}
