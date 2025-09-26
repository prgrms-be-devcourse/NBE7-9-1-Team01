package com.back.domain.payment.entity;

import com.back.domain.order.entity.Order;
import com.back.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Long amount;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
}
