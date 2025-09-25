package com.back.api.product.service;

import com.back.api.product.dto.request.ProductCreateRequest;
import com.back.api.product.dto.request.ProductUpdateRequest;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long count() {
        return productRepository.count();
    }

    @Transactional
    public void saveAll(List<ProductCreateRequest> requests) {
        List<Product> productList = requests.stream()
                .map(request -> Product.builder()
                        .name(request.name())
                        .description(request.description())
                        .price(request.price())
                        .category(request.category())
                        .build())
                .toList();
        productRepository.saveAll(productList);
    }

    @Transactional
    public Product update(Long id, ProductUpdateRequest request) {
        Product product = getProduct(id);
        product.updateDescription(request);
        return product;
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public List<Product> getAll() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        return productList;
    }
}
