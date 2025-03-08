package com.kt.esports.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverNewsService {

	private final WebClient naverNewsWebClient;

	@Value("${naver.api.client-id}") // 네이버 API Client ID
	private String clientId;

	@Value("${naver.api.client-secret}") // 네이버 API Client Secret
	private String clientSecret;

	// 네이버 뉴스 검색 요청
	public Map<String, Object> getNews(String homeTeam, String awayTeam, LocalDate matchDate) throws Exception {

		// 검색어 구성 (대회명 홈팀 원정팀)
		String query = "LCK컵" + homeTeam + " " + awayTeam;
		System.out.println("검색어: " + query);

		// 네이버 뉴스 API 호출
		return naverNewsWebClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/news.json") // 네이버 뉴스 API 엔드포인트
						.queryParam("query", query) // 검색어
						.queryParam("display", 5) // 기사 최대 5개 가져오기
						.queryParam("sort", "sim") // 정확도순 정렬
						.build())
				.header("X-Naver-Client-Id", clientId)
				.header("X-Naver-Client-Secret", clientSecret)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				})
				.block();
	}
}
