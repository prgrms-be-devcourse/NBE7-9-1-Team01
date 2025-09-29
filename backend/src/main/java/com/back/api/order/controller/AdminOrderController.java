package com.back.api.order.controller;

import com.back.api.order.dto.requset.SalesRequest;
import com.back.api.order.dto.response.OrderStatusResponse;
import com.back.api.order.dto.response.SalesResponse;
import com.back.api.order.service.OrderService;
import com.back.global.dto.requset.PageRequestDto;
import com.back.global.dto.response.ApiResponse;
import com.back.global.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Tag(name = "4. [관리자 - 주문]", description = "관리자 주문 관련 API입니다.")
public class AdminOrderController {

    private final OrderService orderService;

    // 주문 통계
    @GetMapping("/stats")
    @Operation(summary = "주문 통계 API", description = "주문 통계를 조회하는 API입니다.")
    public ApiResponse<PageResponse<OrderStatusResponse>> getOrderStats(@RequestBody PageRequestDto request) {
        PageResponse<OrderStatusResponse> responses = orderService.getOrderStats(request);
        return ApiResponse.ok("주문 통계 조회 성공",responses);
    }

    @Operation(summary = "판매량 조회 API", description = "판매량을 조회하는 API입니다.")
    @GetMapping("/sales")
    public ApiResponse<List<SalesResponse>> getSales(
            @Parameter(description = "조회 시작 날짜", example = "2023-01-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

            @Parameter(description = "조회 종료 날짜", example = "2023-01-31")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        SalesRequest request = new SalesRequest(startDate, endDate);
        List<SalesResponse> response = orderService.getSales(request);
        return ApiResponse.ok("제품 판매량 데이터입니다.", response);
    }
}
