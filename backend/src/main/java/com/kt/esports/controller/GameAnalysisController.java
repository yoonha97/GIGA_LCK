package com.kt.esports.controller;

import com.kt.esports.service.GameAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class GameAnalysisController {

	private final GameAnalysisService gameAnalysisService;

	public GameAnalysisController(GameAnalysisService gameAnalysisService) {
		this.gameAnalysisService = gameAnalysisService;
	}

	@GetMapping("/player/{gameName}")
	public ResponseEntity<Map<String, String>> analyzePlayerPerformance(
			@PathVariable String gameName,
			@RequestParam(required = false, defaultValue = "KR1") String tagLine,
			@RequestParam(required = false, defaultValue = "10") int matchCount) {

		String analysis = gameAnalysisService.analyzePlayerPerformance(gameName, tagLine, matchCount);

		Map<String, String> response = new HashMap<>();
		response.put("gameName", gameName);
		response.put("tagLine", tagLine);
		response.put("analysis", analysis);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/match/{matchId}")
	public ResponseEntity<Map<String, String>> analyzeMatchDetail(
			@PathVariable String matchId,
			@RequestParam String gameName,
			@RequestParam(required = false, defaultValue = "KR1") String tagLine) {

		String analysis = gameAnalysisService.analyzeMatchDetail(matchId, gameName, tagLine);

		Map<String, String> response = new HashMap<>();
		response.put("matchId", matchId);
		response.put("gameName", gameName);
		response.put("tagLine", tagLine);
		response.put("analysis", analysis);

		return ResponseEntity.ok(response);
	}
}