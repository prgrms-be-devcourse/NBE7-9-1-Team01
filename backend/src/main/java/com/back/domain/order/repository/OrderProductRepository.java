package com.back.domain.order.repository;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>,OrderProductRepositoryCustom {
    List<OrderProduct> findByOrder(Order order);
}
