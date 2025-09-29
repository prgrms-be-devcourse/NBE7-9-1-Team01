package com.back.api.order.controller;

import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrderProductRepository;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    void setupTestData() {
        // 1. Member 생성
        Member member = memberRepository.save(new Member("test@naver.com", null, "test", "12345", null));

        // 2. Product 생성
        Product product1 = productRepository.findById(1L).orElse(null);
        Product product2 = productRepository.findById(2L).orElse(null);

        // 3. Order 생성
        Order order1 = orderRepository.save(new Order(member, OrderStatus.CANCELED, LocalDateTime.of(2025, 8, 15, 10, 0)));// 기간 밖 주문
        Order order2 = orderRepository.save(new Order(member, OrderStatus.CANCELED, LocalDateTime.of(2025, 9, 15, 10, 1)));

        // 4. OrderProduct 생성
        orderProductRepository.save(new OrderProduct(order1, product1, 50L));
        orderProductRepository.save(new OrderProduct(order1, product2, 20L));
        orderProductRepository.save(new OrderProduct(order2, product1, 100L));
        orderProductRepository.save(new OrderProduct(order2, product2, 55L));
    }

    @Nested
    @DisplayName("판매량 조회 API")
    class t1 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {
            // given
            LocalDate start = LocalDate.of(2025, 9, 1);
            LocalDate end = LocalDate.of(2025, 9, 30);
            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/admin/order/sales")
                            .param("startDate", String.valueOf(start))
                            .param("endDate", String.valueOf(end))
                            .accept(MediaType.APPLICATION_JSON)
            );
            //then
            resultActions
                    .andExpect(handler().handlerType(AdminOrderController.class))
                    .andExpect(handler().methodName("getSales"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].productName").value("Columbia Narino"))
                    .andExpect(jsonPath("$.data[0].totalQuantity").value(100))
                    .andDo(print());
        }
    }
}