package com.back.api.order.controller;

import com.back.api.order.dto.response.OrderStatusResponse;
import com.back.api.order.service.OrderService;
import com.back.global.dto.requset.PageRequestDto;
import com.back.global.dto.response.ApiResponse;
import com.back.global.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Tag(name = "4. [관리자 - 주문]", description = "관리자 주문 관련 API입니다.")
public class AdminOrderController {

    private final OrderService orderService;

    // 주문 통계
    @GetMapping("/stats")
    @Operation(summary = "주문 통계 API", description = "주문 통계를 조회하는 API입니다.")
    public ApiResponse<PageResponse<OrderStatusResponse>> getOrderStats(PageRequestDto request) {
        PageResponse<OrderStatusResponse> responses = orderService.getOrderStats(request);
        return ApiResponse.ok("주문 통계 조회 성공",responses);
    }
}
