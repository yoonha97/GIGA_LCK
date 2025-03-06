package com.kt.esports.controller;

import com.kt.esports.dto.MatchDTO;
import com.kt.esports.service.YouTubeService;
import com.kt.esports.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/matches/{matchId}/youtube")
@RequiredArgsConstructor
public class YouTubeController {

	private final YouTubeService youTubeService;
	private final MatchService matchService;

	// 특정 경기의 유튜브 다시보기 링크 조회
	@GetMapping
	public ResponseEntity<Map<String, String>> getReplayLink(@PathVariable Long matchId) {
		// 경기 정보 조회
		MatchDTO match = matchService.getMatchById(matchId);

		// 유효성 검사 (팀명 또는 날짜가 없을 경우 예외 방지)
		if (match == null || match.getHomeTeam() == null || match.getAwayTeam() == null || match.getDate() == null) {
			return ResponseEntity.badRequest().body(Map.of("youtubeUrl", "Invalid match data"));
		}

		// YouTube 링크 가져오기
		String replayUrl = youTubeService.getReplayLink(
				match.getHomeTeam(), match.getAwayTeam(), match.getDate());

		return ResponseEntity.ok(Map.of("youtubeUrl", replayUrl));
	}
}
