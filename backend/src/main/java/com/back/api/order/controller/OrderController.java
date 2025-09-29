package com.back.api.order.controller;

import com.back.api.order.dto.OrderDto;
import com.back.api.order.service.OrderService;
import com.back.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Tag(name = "2. [제품]", description = "주문 관련 API입니다.")
public class OrderController {

    private final OrderService orderService;


    //주문 등록
    @Schema(name = "OrderCreateReqBody", description = "주문 생성 요청 DTO")
    record OrderCreateReqBody(

            @Schema(description = "회원 이메일", example = "user1@naver.com")
            @NotNull(message = "회원 이메일은 필수입니다.")
            String email,

            @Schema(description = "상품 ID", example = "1")
            @NotNull(message = "상품 ID는 필수입니다.")
            Long productId,
      
            @Schema(description = "주소", example = "경기도 부천")
            String address,
      
            @Schema(description = "우편 번호", example = "55555")
            String postcode,

            @Schema(description = "주문 수량", example = "2")
            Long quantity
    ){}

    @PostMapping
    @Operation(summary = "주문 생성 API", description = "주문을 생성합니다.")
    public ApiResponse<OrderDto> createOrder(
            @RequestBody @Valid OrderCreateReqBody reqBody
    ){
        OrderDto orderDto = orderService.createOrder(reqBody.email(), reqBody.address(), reqBody.postcode(), reqBody.productId(), reqBody.quantity());

        return ApiResponse.ok("주문 생성 완료", orderDto
        );
    }


    //주문 수정
    @Schema(name = "OrderUpdateReqBody", description = "주문 수정 요청 DTO")
    record OrderUpdateReqBody(
            @Schema(description = "상품 ID", example = "1")
            @NotNull(message = "상품 ID는 필수입니다.")
            Long productId,

            @Schema(description = "수량", example = "3")
            @NotNull(message = "수량은 필수입니다.")
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
