package com.back.api.order.dto;

import com.back.domain.order.entity.OrderProduct;

/**
 * 주문 상품 정보를 담는 DTO
 */
public record OrderProductDto(
        Long productId,      // 상품 ID
        String productName,  // 상품 이름
        Long quantity        // 수량
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
