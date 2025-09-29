package com.back.domain.order.repository;

import com.back.domain.member.entity.Member;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Order o SET o.orderStatus = :shipped " +
            "WHERE o.orderStatus = :paid " +
            "AND o.orderDate BETWEEN :start AND :end")
    int updateStatusToShipped(
            @Param("paid") OrderStatus paid,
            @Param("shipped") OrderStatus shipped,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
}
