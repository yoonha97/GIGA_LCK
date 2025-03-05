package com.kt.esports.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeService {

	private final WebClient webClient;

	@Value("${youtube.api.key}")
	private String apiKey;

	private static final String LCK_CHANNEL_NAME = "LCK"; // LCK 공식 채널 이름

	/**
	 * 특정 경기의 다시보기 영상 검색 (LCK 공식 채널)
	 */
	public String getReplayLink(String homeTeam, String awayTeam, LocalDate date) {
		// 날짜를 MM.DD 형식으로 변환
		String formattedDate = date.format(DateTimeFormatter.ofPattern("MM.dd"));

		// 유튜브 검색어 구성 ("T1 vs GEN 02.01 하이라이트")
		String query = String.format("%s vs %s %s 하이라이트", homeTeam, awayTeam, formattedDate);

		// 요청 URL 로그 출력
		System.out.println("🔍 요청 URL: https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query
				+ "&type=video&maxResults=5&order=date&key=" + apiKey);

		try {
			Map<String, Object> response = webClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/search")
							.queryParam("part", "snippet")
							.queryParam("q", query)
							.queryParam("type", "video")
							.queryParam("maxResults", 5) // 여러 개 검색 후 필터링
							.queryParam("order", "date")
							.queryParam("key", apiKey)
							.build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
					})
					.block();

			// API 응답 로그 출력
			System.out.println("📌 API 응답: " + response);

			Object itemsObject = response.get("items");

			if (itemsObject instanceof List<?>) {
				List<?> rawList = (List<?>) itemsObject;
				List<Map<String, Object>> items = rawList.stream()
						.filter(item -> item instanceof Map)
						.map(item -> (Map<String, Object>) item)
						.filter(item -> {
							// LCK 공식 채널에서 업로드된 영상인지 확인
							Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
							String channelTitle = (String) snippet.get("channelTitle");
							return channelTitle != null && channelTitle.equalsIgnoreCase(LCK_CHANNEL_NAME);
						})
						.toList();

				if (!items.isEmpty()) {
					String videoId = (String) ((Map<String, Object>) items.get(0).get("id")).get("videoId");
					return "https://www.youtube.com/watch?v=" + videoId;
				}
			}

			return "No replay found"; // 다시보기 영상이 없을 경우
		} catch (Exception e) {
			System.err.println("❌ API 호출 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			return "Error retrieving replay link"; // 에러 발생 시 반환값
		}
	}
}
