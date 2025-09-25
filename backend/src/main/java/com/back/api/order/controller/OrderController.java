package com.back.api.order.controller;

import com.back.api.order.dto.OrderDto;
import com.back.api.order.service.OrderService;
import com.back.domain.order.entity.Order;
import com.back.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;


    //주문 등록

    record OrderCreateReqBody(
            String email,
            Long productId,
            Long quantity
    ){}

    @PostMapping
    @Transactional
    public ApiResponse<OrderDto> createOrder(
            @RequestBody @Valid OrderCreateReqBody reqBody
    ){
        Order order = orderService.createOrder(reqBody.email(), reqBody.productId(), reqBody.quantity());
        OrderDto orderDto = new OrderDto(order);

        return ApiResponse.ok("주문 생성 완료", orderDto
        );
    }

}
