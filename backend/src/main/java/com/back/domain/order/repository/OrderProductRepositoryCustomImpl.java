package com.back.domain.order.repository;

import com.back.api.order.dto.response.SalesResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.back.domain.order.entity.QOrderProduct.orderProduct;

@RequiredArgsConstructor
public class OrderProductRepositoryCustomImpl implements OrderProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SalesResponse> getSalesByProduct(LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        SalesResponse.class,
                        orderProduct.product.name.as("productName"),
                        orderProduct.quantity.sum().as("totalQuantity")
                ))
                .from(orderProduct)
                .where(orderProduct.order.orderDate.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .groupBy(orderProduct.product)
                .fetch();
    }
}
