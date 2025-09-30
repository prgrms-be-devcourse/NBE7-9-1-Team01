package com.back.api.order.service;


import com.back.api.order.dto.response.SalesResponse;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.repository.OrderProductRepository;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    public List<OrderProduct> findByOrder(Order order) {
        return orderProductRepository.findByOrder(order);

    }

    public List<OrderProduct> getOrderProductByOrder(Order order) {
        List<OrderProduct> result = orderProductRepository.findByOrder(order);
        if (result.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_ORDER);
        }
        return result;
    }

    public List<SalesResponse> getSales(LocalDate startDate, LocalDate endDate) {
        return orderProductRepository.getSalesByProduct(startDate, endDate);
    }

    public void removeAllByorder(Order order) {
        orderProductRepository.deleteAllByOrder(order);
    }
}
