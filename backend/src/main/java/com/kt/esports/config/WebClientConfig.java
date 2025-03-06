package com.kt.esports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	// YouTube API WebClient
	@Bean
	public WebClient youtubeWebClient() {
		return WebClient.builder()
				.baseUrl("https://www.googleapis.com/youtube/v3")
				.build();
	}

	// 네이버 뉴스 API WebClient
	@Bean
	public WebClient naverNewsWebClient() {
		return WebClient.builder()
				.baseUrl("https://openapi.naver.com/v1/search")
				.build();
	}
}
