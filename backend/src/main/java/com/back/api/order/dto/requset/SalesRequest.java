package com.back.api.order.dto.requset;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "SalesRequest", description = "판매량 조회 요청 dto")
public record SalesRequest(
        @Schema(description = "조회 시작 날짜", example = "2023-01-01")
        LocalDate startDate,
        @Schema(description = "조회 종료 날짜", example = "2023-01-31")
        LocalDate endDate
) {
}
