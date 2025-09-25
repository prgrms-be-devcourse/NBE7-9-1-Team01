package com.back.api.product.controller;

import com.back.api.product.dto.request.ProductUpdateRequest;
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
            long targetProductId = 10000;
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
}