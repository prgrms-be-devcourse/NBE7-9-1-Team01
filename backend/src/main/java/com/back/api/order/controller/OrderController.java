package com.back.api.order.controller;

import com.back.api.order.dto.OrderDto;
import com.back.api.order.service.OrderService;
import com.back.domain.order.entity.Order;
import com.back.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //주문 수정
    record OrderUpdateReqBody(
            String email,
            Long productId,
            Long quantity
    ){}

    @PutMapping("{orderId}")
    public ApiResponse<Void> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateReqBody reqBody
    ){
        Order order = orderService.findOrderById(orderId).get();
        orderService.updateOrder(reqBody.email(),
                reqBody.productId(),
                reqBody.quantity()
        );
        return  ApiResponse.ok(
                "%d번 주문이 수정되었습니다".formatted(orderId), null
        );
    }

    // 주문 조회
    // 주문 단일 조회
    @GetMapping("/{orderId}")
    @Transactional(readOnly = true)
    public OrderDto getOrder(
            @PathVariable Long orderId
    ){
        Order order = orderService.findOrderById(orderId).get();
        OrderDto orderDto = new OrderDto(order);
        return orderDto;
    }

    //전체 주문 조회
    @GetMapping
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(){
        return orderService.findAll().stream()
                .map(OrderDto::new)
                .toList();
    }


}
