package com.back.api.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SalesResponse", description = "판매량 조회 응답 dto")
public record SalesResponse(
    @Schema(description = "제품 이름", example = "Columbia Narino")
    String productName,
    @Schema(description = "제품의 총 판매량", example = "100")
    Long totalQuantity
) {
}
