package com.back.api.product.controller;

import com.back.api.product.dto.request.ProductUpdateRequest;
import com.back.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("제품 수정 API")
    class t1 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {

            // given
            long targetProductId = 1;
            ProductUpdateRequest request = new ProductUpdateRequest("콜롬비아 커피 원두 입니다.");

            // when
            ResultActions resultActions = mockMvc.perform(
                    patch("/api/products/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("update"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.description").value("콜롬비아 커피 원두 입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("valid 문제")
        void fail1() throws Exception {

            // given
            long targetProductId = 1;
            ProductUpdateRequest request = new ProductUpdateRequest(null);

            // when
            ResultActions resultActions = mockMvc.perform(
                    patch("/api/products/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("update"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("설명이 비어있습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("product가 존재하지 않을 때")
        void fail2() throws Exception {

            // given
            long targetProductId = 0;
            ProductUpdateRequest request = new ProductUpdateRequest("콜롬비아 커피 원두 입니다.");

            // when
            ResultActions resultActions = mockMvc.perform(
                    patch("/api/products/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("update"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("제품을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("제품 다건 조회 API")
    class t2 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {


            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("getAll"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].name").value("Columbia Narino"))
                    .andExpect(jsonPath("$.data[0].price").value(5000L))
                    .andExpect(jsonPath("$.data[0].category").value("커피콩"))
                    .andExpect(jsonPath("$.data[1].id").value(2))
                    .andExpect(jsonPath("$.data[1].name").value("Brazil Serra Do Caparao"))
                    .andExpect(jsonPath("$.data[1].price").value(7000L))
                    .andExpect(jsonPath("$.data[1].category").value("커피콩"))
                    .andDo(print());
        }

        @Test
        @DisplayName("데이터가 존재안할 때")
        void fail1() throws Exception {
            productRepository.deleteAll();

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/products")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("getAll"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("제품을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("제품 단건 조회 API")
    class t3 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {

            // given
            long targetProductId = 1;

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/products/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("getProduct"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.name").value("Columbia Narino"))
                    .andExpect(jsonPath("$.data.price").value(5000L))
                    .andExpect(jsonPath("$.data.category").value("커피콩"))
                    .andDo(print());
        }

        @Test
        @DisplayName("product가 존재하지 않을 때")
        void fail1() throws Exception {

            // given
            long targetProductId = 0;

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/products/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("getProduct"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("제품을 찾을 수 없습니다."))
                    .andDo(print());
        }
    }
}