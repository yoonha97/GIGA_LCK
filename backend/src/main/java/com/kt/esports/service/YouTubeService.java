package com.kt.esports.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YouTubeService {

	private final WebClient youtubeWebClient;

	private static final String LCK_CHANNEL_NAME = "LCK"; // LCK 공식 채널 이름

	// 특정 경기의 다시보기 영상 검색 (LCK 공식 채널)
	public String getReplayLink(String homeTeam, String awayTeam, LocalDate matchDate) {
		// 날짜 포맷팅
		String formattedDate = matchDate.format(DateTimeFormatter.ofPattern("MM.dd"));
		// 검색 쿼리 생성
		String query = String.format("%s vs %s %s 하이라이트", homeTeam, awayTeam, formattedDate);

		try {
			Map<String, Object> response = youtubeWebClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/search")
							.queryParam("part", "snippet")
							.queryParam("q", query)
							.queryParam("type", "video")
							.queryParam("maxResults", 5)
							.queryParam("order", "relevance")
							.build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
					})
					.block();

			// 유튜브 응답 데이터에서 "items" 추출
			Object itemsObject = response.get("items");

			if (itemsObject instanceof List<?> rawList) {
				List<Map<String, Object>> items = rawList.stream()
						.filter(item -> item instanceof Map)
						.map(item -> (Map<String, Object>) item)
						.filter(item -> {
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

			// 다시보기 영상이 없으면 JSON 응답을 위해 문자열 반환
			return "No replay found";

		} catch (Exception e) {
			e.printStackTrace();
			return "Error retrieving replay link";
		}
	}
}
