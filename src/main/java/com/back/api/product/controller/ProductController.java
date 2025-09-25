package com.back.api.product.controller;

import com.back.api.product.dto.request.ProductUpdateRequest;
import com.back.api.product.service.ProductService;

import com.back.domain.product.entity.Product;
import com.back.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "1. [제품]", description = "제품 관련 API입니다.")
public class ProductController {

    private final ProductService productService;

    @PatchMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        Product product = productService.update(id,request);
        return ApiResponse.ok("%d의 제품이 수정되었습니다.".formatted(id),product);
    }
}
