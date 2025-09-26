package com.back.api.payment.controller;

import com.back.api.payment.dto.request.PaymentCreateRequest;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrderProductRepository;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.payment.entity.PaymentMethod;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;


    @BeforeAll
    void setUp() {
        Member member = new Member("test@naver.com", null, "경기도 부천", "55555", Role.ROLE_USER);
        memberRepository.save(member);
        Product product1 = productRepository.findById(1L).orElse(null);
        Product product2 = productRepository.findById(2L).orElse(null);
        Order order = new Order(member, OrderStatus.PENDING, LocalDate.now());
        Order order1 = new Order(member, OrderStatus.CANCELED, LocalDate.now());
        orderRepository.save(order);
        orderRepository.save(order1);

        OrderProduct orderProduct1 = new OrderProduct(order, product1, 2L);
        OrderProduct orderProduct2 = new OrderProduct(order, product2, 3L);
        OrderProduct orderProduct3 = new OrderProduct(order1, product2, 3L);

        orderProductRepository.save(orderProduct1);
        orderProductRepository.save(orderProduct2);
        orderProductRepository.save(orderProduct3);
    }


    @Nested
    @DisplayName("결제 생성 API")
    class t1 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {

            // given
            PaymentCreateRequest request = new PaymentCreateRequest(1L, PaymentMethod.CREDIT_CARD);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/payments")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(PaymentController.class))
                    .andExpect(handler().methodName("createPayment"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.paymentId").value(1))
                    .andExpect(jsonPath("$.data.method").value("CREDIT_CARD"))
                    .andExpect(jsonPath("$.data.amount").value(31000))
                    .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                    .andExpect(jsonPath("$.data.postcode").value("55555"))
                    .andExpect(jsonPath("$.data.address").value("경기도 부천"))
                    .andDo(print());
        }

        @Test
        @DisplayName("Order가 존재하지 않을 때")
        void fail1() throws Exception {

            // given
            PaymentCreateRequest request = new PaymentCreateRequest(0L, PaymentMethod.CREDIT_CARD);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/payments")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(PaymentController.class))
                    .andExpect(handler().methodName("createPayment"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("주문을 찾을 수 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("OrderStatus가 PENDING이 아닐 때")
        void fail2() throws Exception {

            // given
            PaymentCreateRequest request = new PaymentCreateRequest(2L, PaymentMethod.CREDIT_CARD);


            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/payments")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(PaymentController.class))
                    .andExpect(handler().methodName("createPayment"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("결제 가능한 상태가 아닙니다."))
                    .andDo(print());
        }
    }

}