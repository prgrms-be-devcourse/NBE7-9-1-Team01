package com.back.api.order.service;

import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.repository.OrderProductRepository;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;


    public List<OrderProduct> getOrderProductByOrderId(Long orderId) {
        List<OrderProduct> result = orderProductRepository.findByOrderId(orderId);
        if (result.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_ORDER);
        }
        return result;
    }
}
