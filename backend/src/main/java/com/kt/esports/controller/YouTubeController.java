package com.kt.esports.controller;

import com.kt.esports.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YouTubeController {

	private final YouTubeService youTubeService;

	@GetMapping("/replay")
	public String getReplayLink(
			@RequestParam String homeTeam,
			@RequestParam String awayTeam,
			@RequestParam String date) {

		LocalDate matchDate = LocalDate.parse(date); // 날짜 변환
		return youTubeService.getReplayLink(homeTeam, awayTeam, matchDate);
	}
}
