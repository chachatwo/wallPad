package com.wallpad.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI wallpadOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WallPad 아파트 시스템 API 문서")
                        .description("하자보수, 차량예약, 로그인 등 주요 기능 API 명세서")
                        .version("v1.0"));
    }
}
