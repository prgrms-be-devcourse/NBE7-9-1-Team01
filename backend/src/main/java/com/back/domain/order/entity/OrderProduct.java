package com.back.domain.order.entity;

import com.back.domain.payment.entity.Payment;
import com.back.domain.product.entity.Product;
import com.back.global.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long quantity;

    public void updateQuantity(Long quantity){
        this.quantity = quantity;
    }

//    public void setCategory(String category) {
//        this.product.setCategory(category);
//    }


    public void setOrder(Order order) {
        this.order = order;
    }
//    public OrderProduct(Product product) {
//        this.product = product;
//        this.quantity = 1L;
//    }
//public OrderProduct(Order order, Product product) {
//    this(order, product, 1L);
//}

//    public OrderProduct(Order order, Product product, Long quantity) {
//        this.order = order;
//        this.product = product;
//        this.quantity = quantity != null && quantity > 0 ? quantity : 1L;
//    }


}
