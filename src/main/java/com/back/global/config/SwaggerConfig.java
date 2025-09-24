package com.back.global.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Caffe Menu API 명세서")
                        .version("v0.0.1")
                        .description("카페 메뉴 관리 애플리케이션입니다."));
    }


}
