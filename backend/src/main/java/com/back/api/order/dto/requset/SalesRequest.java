package com.back.api.order.dto.requset;

import java.time.LocalDate;

public record SalesRequest(
        LocalDate startDate,
        LocalDate endDate
) {
}
