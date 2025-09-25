package com.back.domain.order.entity;

import com.back.domain.member.entity.Member;
import com.back.domain.product.entity.Product;
import com.back.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDate orderDate;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true, fetch = FetchType.LAZY)
    private List<OrderProduct> orders = new ArrayList<>();


    public Optional<OrderProduct> findOrderProductById(Long orderProductId) {
        return orders.stream()
                .filter(o -> o.getId().equals(orderProductId))
                .findFirst();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProduct.setOrder(this);
        this.orders.add(orderProduct);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        this.orders.remove(orderProduct);
    }
//
//    public void updateOrderStatus(OrderStatus orderStatus) {
//        this.orderStatus = orderStatus;
//    }

    public Order(Member member) {
        this.member = member;
        this.orderStatus = OrderStatus.PENDING;
        this.orders = new ArrayList<>();
    }

    public void setOrderDate(LocalDate now) {
        this.orderDate = now;
    }

    public void updateProduct(Product product, long quantity) {
        for(OrderProduct orderProduct : orders){
            if(orderProduct.getProduct().equals(product)){
                orderProduct.updateQuantity(quantity);
                return;
            }
        }
    }


    public List<OrderProduct> getOrderProducts() {
        return this.orders; // orders는 Order 안에 있는 List<OrderProduct>
    }

}
