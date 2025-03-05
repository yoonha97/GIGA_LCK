package com.kt.esports.controller;

import com.kt.esports.dto.MatchDTO;
import com.kt.esports.service.MatchService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

	private final MatchService matchService;

	/**
	 * 1. 전체 경기 조회 API
	 */
	@GetMapping
	public ResponseEntity<List<MatchDTO>> getAllMatches() {
		List<MatchDTO> matches = matchService.getAllMatches();
		return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
	}

	/**
	 * 2. 월별 경기 조회 API
	 */
	@GetMapping("/month")
	public List<MatchDTO> getMatchesByMonth(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month) {
		return matchService.getMatchesByMonth(year, month);
	}

	/**
	 * 3. 특정 팀 경기 조회 API
	 */
	@GetMapping("/team")
	public List<MatchDTO> getMatchesByTeam(
			@RequestParam String teamName) {
		return matchService.getMatchesByTeam(teamName);
	}
}
