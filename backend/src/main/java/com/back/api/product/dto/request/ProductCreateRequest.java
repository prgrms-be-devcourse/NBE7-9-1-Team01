package com.back.api.product.dto.request;

public record ProductCreateRequest(
        String name,
        String description,
        Long price,
        String category
) {
}
