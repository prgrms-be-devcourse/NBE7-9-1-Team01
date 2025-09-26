package com.back.global.config;

import com.back.api.product.dto.request.ProductCreateRequest;
import com.back.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitDataConfig {

    private final ProductService productService;

    @Bean
    ApplicationRunner initData() {
        return args -> {
            work();
        };
    }

    private void work() {
        if (productService.count() > 0)
            return;

        ProductCreateRequest request1 = new ProductCreateRequest("Columbia Narino", null, 5000L, "커피콩");
        ProductCreateRequest request2 = new ProductCreateRequest("Brazil Serra Do Caparao", null, 7000L, "커피콩");
        ProductCreateRequest request3 = new ProductCreateRequest("Columbia Quindio (White Wine Extended Fermentation)", null, 10000L, "커피콩");
        ProductCreateRequest request4 = new ProductCreateRequest("Ethiopia Sidamo", null, 4000L, "커피콩");
        List<ProductCreateRequest> requests = List.of(request1, request2, request3, request4);

        productService.saveAll(requests);

    }
}
