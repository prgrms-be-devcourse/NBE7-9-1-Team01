package com.back.api.product.controller;

import com.back.api.product.service.ProductService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Nested
    @DisplayName("제품 API 수정")
    class t1 {
        @Test
        @DisplayName("정상 작동")
        void success() throws Exception {

            // given
            long targetProductId = 1;
            String description = "콜롬비아 커피 원두 입니다.";

            // when
            ResultActions result = mockMvc.perform(
                    put("api/product/%d".formatted(targetProductId))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(description)
            );

            // then
            result
                    .andExpect(handler().handlerType(ProductController.class))
                    .andExpect(handler().methodName("update"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.description").value("콜롬비아 커피 원두 입니다."))
                    .andDo(print());
        }
    }
}