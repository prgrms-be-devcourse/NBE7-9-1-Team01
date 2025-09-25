package com.back.domain.product.entity;


import com.back.api.product.dto.request.ProductUpdateRequest;
import com.back.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
    private String name;
    private String description;
    private Long price;
    private String category;

    public void updateDescription(ProductUpdateRequest request) {
        this.description = request.description();
    }

}
