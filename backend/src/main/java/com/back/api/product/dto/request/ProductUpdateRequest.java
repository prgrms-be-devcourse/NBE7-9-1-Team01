package com.back.api.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ProductUpdateRequest", description = "제품 수정 dto")
public record ProductUpdateRequest(
        @NotNull(message = "설명이 비어있습니다.")
        @Schema(description = "제품 설명", example = "콜롬비아 커피 원두 입니다.")
        String description

) {
}
