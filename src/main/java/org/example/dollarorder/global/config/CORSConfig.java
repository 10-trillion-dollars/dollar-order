package org.example.dollarorder.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
            .allowedOrigins("http://localhost:8081", "https://10-trillion-dollars.shop") // 허용할 오리진
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
            .allowedHeaders("*") // 허용할 헤더
            .allowCredentials(true) // 쿠키를 포함할지 여부
            .maxAge(3600); // pre-flight 요청의 캐시 시간 (초 단위)
    }
}
