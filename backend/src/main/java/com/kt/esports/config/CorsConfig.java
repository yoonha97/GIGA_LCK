package com.kt.esports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry.addMapping("/api/**") // 모든 API 엔드포인트에 대해 CORS 허용
						.allowedOrigins("http://localhost:5173") // Vue 개발 서버 허용
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
						.allowedHeaders("*") // 모든 요청 헤더 허용
						.allowCredentials(true); // 쿠키 포함 요청 허용 (필요 시)
			}
		};
	}
}
