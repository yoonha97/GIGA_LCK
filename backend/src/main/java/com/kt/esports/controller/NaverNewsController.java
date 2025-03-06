package com.kt.esports.controller;

import com.kt.esports.service.NaverNewsService;
import com.kt.esports.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/matches/{matchId}/news")
@RequiredArgsConstructor
public class NaverNewsController {

	private final NaverNewsService naverNewsService;
	private final MatchService matchService;

	// 특정 경기의 네이버 뉴스 조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> getMatchNews(@PathVariable Long matchId) throws Exception {
		// 경기 정보 조회
		var match = matchService.getMatchById(matchId);
		LocalDate matchDate = match.getDate(); // 경기 날짜 가져오기

		// 네이버 뉴스 API 호출
		Map<String, Object> newsData = naverNewsService.getNews(
				match.getHomeTeam(), match.getAwayTeam(), matchDate);

		return ResponseEntity.ok(newsData);
	}
}
