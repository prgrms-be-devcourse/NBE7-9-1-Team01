package com.back.api.product.controller;

import com.back.api.product.dto.request.ProductUpdateRequest;
import com.back.api.product.service.ProductService;

import com.back.domain.product.entity.Product;
import com.back.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "1. [제품]", description = "제품 관련 API입니다.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "제품 수정 API", description = "제품의 설명란을 수정합니다.")
    @PatchMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        Product product = productService.update(id,request);
        return ApiResponse.ok("%d번 제품이 수정되었습니다.".formatted(id),product);
    }

    @Operation(summary = "제품 다건 조회 API", description = "제품 리스트를 조회합니다..")
    @GetMapping("")
    public ApiResponse<List<Product>> getAll() {
        List<Product> productList = productService.getAll();
        return ApiResponse.ok("제품 리스트 데이터입니다.",productList);
    }

    @Operation(summary = "제품 단건 조회 API", description = "제품을 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ApiResponse.ok("%d번 제품입니다.".formatted(id),product);
    }
}
