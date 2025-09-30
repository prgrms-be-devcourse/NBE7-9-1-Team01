package com.back.api.order.service;


import com.back.api.member.service.MemberService;
import com.back.api.order.dto.OrderDto;
import com.back.api.order.dto.requset.SalesRequest;
import com.back.api.order.dto.response.OrderStatusResponse;
import com.back.api.order.dto.response.SalesResponse;
import com.back.api.product.service.ProductService;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.product.entity.Product;
import com.back.global.dto.requset.PageRequestDto;
import com.back.global.dto.response.PageResponse;
import com.back.global.exception.ErrorCode;
import com.back.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final MemberRepository memberRepository;


    @Transactional
    public int dailyOrderProcess() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime start = yesterday.withHour(14).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(14).withMinute(0).withSecond(0);

        // PAID -> SHIPPED 처리 및 처리 완료된 주문 개수 반환
        int processComplete = orderRepository.updateStatusToShipped(OrderStatus.PAID, OrderStatus.SHIPPED, start, end);

        return processComplete;
    }

    @Transactional
    public OrderDto createOrder(String email, String address, String postcode, long productId, long quantity) {

        if(!memberRepository.existsByEmail(email)) {
            Member newMember = new Member(email, null, Role.ROLE_USER);
            memberRepository.save(newMember);
        }

        Member member = memberService.findByEmail(email);
        Product product = productService.findById(productId);

//        Order order = new Order(member, LocalDate.now(), OrderStatus.PENDING);
        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .address(address)
                .postcode(postcode)
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
        orderProductService.removeAllByorder(order);
        orderRepository.delete(order);
    }


    public Order getId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_ORDER));
    }

    // 결제를 위한 체크 메소드
    @Transactional
    public void validateOrderStatus(Order order, OrderStatus beforeStatus,OrderStatus afterStatus) {
        if(!order.getOrderStatus().equals(beforeStatus))
            throw new ErrorException(ErrorCode.INVALID_ORDER_STATE);

        order.updateOrderStatus(afterStatus);
    }

    public PageResponse<OrderStatusResponse> getOrderStats(PageRequestDto request) {
        PageRequestDto requestDto = validRequest(request);
        Pageable pageable = getPageable(requestDto);
        Page<OrderStatusResponse> orderPage = orderRepository.findAllByOrderByOrderDateDesc(pageable)
                .map(OrderStatusResponse::from);
        return PageResponse.of(orderPage);
    }

    private PageRequestDto validRequest(PageRequestDto request) {
        if(request.sort() == null || request.sort().isBlank()){
            request = new PageRequestDto(request.page(), request.size(), "orderDate", "desc");
        }
        return request;
    }

    private Pageable getPageable(PageRequestDto request) {
        return request.toPageable();
    }

    public List<SalesResponse> getSales(SalesRequest request) {
        SalesRequest requestDto = validSalesRequest(request);
        return orderProductService.getSales(requestDto.startDate(), requestDto.endDate());
    }

    private SalesRequest validSalesRequest(SalesRequest request) {
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        if (startDate.isAfter(endDate)) {
            throw new ErrorException(ErrorCode.INVALID_DATE_RANGE);
        }

        return request;
    }
}
