package com.back.domain.order.repository;

import com.back.api.order.dto.response.SalesResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderProductRepositoryCustom {
    List<SalesResponse> getSalesByProduct(LocalDate startDate, LocalDate endDate);
}
