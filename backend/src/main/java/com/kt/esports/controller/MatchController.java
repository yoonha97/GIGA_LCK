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

	// 전체 경기 조회
	@GetMapping
	public ResponseEntity<List<MatchDTO>> getAllMatches() {
		List<MatchDTO> matches = matchService.getAllMatches();
		return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
	}

	// 월별 경기 조회
	@GetMapping("/month")
	public ResponseEntity<List<MatchDTO>> getMatchesByMonth(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month) {
		List<MatchDTO> matches = matchService.getMatchesByMonth(year, month);
		return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
	}

	// 특정 팀 경기 조회 (팀 이름 기반)
	@GetMapping("/team")
	public ResponseEntity<List<MatchDTO>> getMatchesByTeam(
			@RequestParam String teamName) {
		List<MatchDTO> matches = matchService.getMatchesByTeam(teamName);
		return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
	}

	// 특정 경기 상세 조회
	@GetMapping("/{matchId}")
	public ResponseEntity<MatchDTO> getMatchById(@PathVariable Long matchId) {
		MatchDTO match = matchService.getMatchById(matchId);
		return ResponseEntity.ok(match);
	}
}
