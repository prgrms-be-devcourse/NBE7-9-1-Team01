package com.back.api.order.controller;

import com.back.api.order.dto.OrderDto;
import com.back.api.order.service.OrderService;
import com.back.domain.order.entity.Order;
import com.back.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Tag(name = "2. [제품]", description = "주문 관련 API입니다.")
public class OrderController {

    private final OrderService orderService;


    //주문 등록

    record OrderCreateReqBody(
            String email,
            Long productId,
            Long quantity
    ){}

    @PostMapping
    @Operation(summary = "주문 생성 API", description = "주문을 생성합니다.")
    public ApiResponse<OrderDto> createOrder(
            @RequestBody @Valid OrderCreateReqBody reqBody
    ){
        OrderDto orderDto = orderService.createOrder(reqBody.email(), reqBody.productId(), reqBody.quantity());

        return ApiResponse.ok("주문 생성 완료", orderDto
        );
    }


    //주문 수정
    record OrderUpdateReqBody(
            Long productId,
            Long quantity
    ){}
    @Operation(summary = "주문 수정 API", description = "주문을 수정합니다.")
    @PutMapping("{orderId}")
    public ApiResponse<Void> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateReqBody reqBody
    ){
        OrderDto orderDto = orderService.updateOrder(orderId, reqBody.productId(), reqBody.quantity());
        return  ApiResponse.ok(
                "%d번 주문이 수정되었습니다".formatted(orderId), null
        );
    }

    // 주문 조회
    // 주문 단일 조회
    @Operation(summary = "주문 다건 조회 API", description = "주문 리스트를 조회합니다.")
    @GetMapping("/{orderId}")
    public OrderDto getOrder(
            @PathVariable Long orderId
    ){
        OrderDto orderDto = orderService.getOrderDto(orderId);
        return orderDto;
    }

    //전체 주문 조회
    @Operation(summary = "주문 다건 조회 API", description = "주문 리스트를 조회합니다.")
    @GetMapping
    public List<OrderDto> getOrders(){
        List<OrderDto> orders = orderService.getAllOrdersDto();
        return orders;
    }

    //주문 삭제
    @Operation(summary = "주문 삭제 API", description = "주문을 수정합니다.")
    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);

        return ApiResponse.ok(
                "%d번 주문이 삭제되었습니다".formatted(orderId), null
        );
    }

}
