package com.kt.esports.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		// 경기 날짜 형식 변환 (YYYYMMDD)
		String formattedDate = matchDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		// 검색어 구성 (홈팀 원정팀)
		String query = homeTeam + " " + awayTeam;
		System.out.println("검색어: " + query);

		// 검색어 URL 인코딩
		String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

		// 네이버 뉴스 API 호출
		return naverNewsWebClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/news.json") // 네이버 뉴스 API 엔드포인트
						.queryParam("query", encodedQuery) // 검색어
						.queryParam("display", 5) // 기사 최대 5개 가져오기
						.queryParam("sort", "sim") // 정확도순 정렬
						.queryParam("dts", formattedDate) // 검색 시작 날짜 (경기 당일)
						.queryParam("dte", formattedDate) // 검색 종료 날짜 (경기 당일)
						.build())
				.header("X-Naver-Client-Id", clientId)
				.header("X-Naver-Client-Secret", clientSecret)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				})
				.block();
	}
}
