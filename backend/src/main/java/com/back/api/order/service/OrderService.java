package com.back.api.order.service;


import com.back.api.order.dto.OrderDto;
import com.back.api.product.service.ProductService;
import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.product.entity.Product;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final MemberService memberService;
    private final ProductService productService;


    @Transactional
    public int dailyOrderProcess() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime start = yesterday.withHour(14).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(14).withMinute(0).withSecond(0);

        // PROCESSING -> SHIPPED 처리 및 처리 완료된 주문 개수 반환
        int processComplete = orderRepository.updateStatusToShipped(OrderStatus.PROCESSING, OrderStatus.SHIPPED, start, end);

        return processComplete;
    }
    @Transactional
    public OrderDto createOrder(String email, long productId, long quantity) {
        Member member = memberService.findByEmail(email);
        Product product = productService.findById(productId);

//        Order order = new Order(member, LocalDate.now(), OrderStatus.PENDING);
        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDate.now())
                .orderStatus(OrderStatus.PENDING)
                .build();
        order = orderRepository.save(order);
        OrderProduct orderProduct = new OrderProduct(order, product, quantity);
        orderProductService.save(orderProduct);

        List<OrderProduct> orderProducts = orderProductService.findByOrder(order);
        return new OrderDto(order, orderProducts);

    }

    //주문 수정(업데이트)
    @Transactional
    public OrderDto updateOrder(long orderId, long productId, long quantity){

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_ORDER));
        Product product = productService.findById(productId);

        OrderProduct orderProduct = orderProductService.findByOrder(order).stream()
                .filter(op -> op.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_ORDER));

        orderProduct.updateQuantity(quantity);
        order = orderRepository.save(order);
        List<OrderProduct> orderProducts = orderProductService.findByOrder(order);

        return new OrderDto(order, orderProducts);
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderDto(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<OrderProduct> orderProducts = orderProductService.findByOrder(order);
        return new OrderDto(order, orderProducts);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrdersDto() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    List<OrderProduct> orderProducts = orderProductService.findByOrder(order);
                    return new OrderDto(order, orderProducts);
                })
                .toList();
    }

    //주문 삭제
    public void deleteOrder(long orderId){
        Order order = orderRepository.findById(orderId).get();

        orderRepository.delete(order);
    }


    public Order getId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_ORDER));
    }

    // 결제를 위한 체크 메소드
    @Transactional
    public void validateOrderStatus(Order order, OrderStatus orderStatus) {
        if(!order.getOrderStatus().equals(orderStatus))
            throw new ErrorException(ErrorCode.INVALID_ORDER_STATE);

        order.updateOrderStatus(OrderStatus.PAID);
    }

}
