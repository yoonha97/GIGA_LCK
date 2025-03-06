package com.kt.esports.controller;

import com.kt.esports.service.YouTubeService;
import com.kt.esports.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/matches/{matchId}/youtube") // 엔드포인트 변경
@RequiredArgsConstructor
public class YouTubeController {

	private final YouTubeService youTubeService;
	private final MatchService matchService;

	@GetMapping
	public ResponseEntity<Map<String, String>> getReplayLink(@PathVariable Long matchId) {
		// 경기 정보를 조회하여 homeTeam, awayTeam, date 가져오기
		var match = matchService.getMatchById(matchId);
		LocalDate matchDate = match.getDate(); // Match 엔티티의 date 필드

		// YouTube 링크 가져오기
		String replayUrl = youTubeService.getReplayLink(
				match.getHomeTeam(), match.getAwayTeam(), matchDate);

		return ResponseEntity.ok(Map.of("youtubeUrl", replayUrl));
	}
}
