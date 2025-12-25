package com.jfintech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả các đường dẫn
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Cho phép các lệnh này
                        .allowedOrigins("*"); // Cho phép truy cập từ bất kỳ đâu (Frontend)
            }
        };
    }
}