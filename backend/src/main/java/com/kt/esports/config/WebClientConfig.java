package com.kt.esports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${openai.api.key}")
	private String openaiApiKey;

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

	// 라이엇 API WebClient (아시아 리전)
	@Bean
	public WebClient riotAsiaWebClient() {
		return WebClient.builder()
				.baseUrl("https://asia.api.riotgames.com")
				.build();
	}

	// 라이엇 API WebClient (한국 리전)
	@Bean
	public WebClient riotKrWebClient() {
		return WebClient.builder()
				.baseUrl("https://kr.api.riotgames.com")
				.build();
	}

	// OpenAI API WebClient
	@Bean
	public WebClient openaiWebClient() {
		return WebClient.builder()
				.baseUrl("https://api.openai.com/v1")
				.defaultHeader("Authorization", "Bearer " + openaiApiKey)
				.defaultHeader("Content-Type", "application/json")
				.build();
	}
}
