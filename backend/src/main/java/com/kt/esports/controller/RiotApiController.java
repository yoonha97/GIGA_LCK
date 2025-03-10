package com.kt.esports.controller;

import com.kt.esports.service.PlayerAccountService;
import com.kt.esports.service.RiotApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/riot")
public class RiotApiController {
	private final RiotApiService riotApiService;
	private final PlayerAccountService playerAccountService;

	public RiotApiController(RiotApiService riotApiService, PlayerAccountService playerAccountService) {
		this.riotApiService = riotApiService;
		this.playerAccountService = playerAccountService;
	}

	@GetMapping("/summoner/{riotId}")
	public ResponseEntity<Map<String, Object>> getSummonerByRiotId(
			@PathVariable String riotId,
			@RequestParam(required = false) String tagLine) {

		// tagLine이 없으면 DB에서 조회
		if (tagLine == null || tagLine.isEmpty()) {
			try {
				tagLine = playerAccountService.getTagLineByRiotId(riotId);
			} catch (IllegalArgumentException e) {
				// DB에 정보가 없으면 기본값 KR1 사용
				tagLine = "KR1";
			}
		}

		String puuid = riotApiService.getPuuidByRiotId(riotId, tagLine);
		Map<String, Object> summoner = riotApiService.getSummonerByPuuid(puuid);

		return ResponseEntity.ok(summoner);
	}

	@GetMapping("/league/{riotId}")
	public ResponseEntity<Map<String, Object>> getLeagueInfoByRiotId(
			@PathVariable String riotId,
			@RequestParam(required = false) String tagLine) {

		// tagLine이 없으면 DB에서 조회
		if (tagLine == null || tagLine.isEmpty()) {
			try {
				tagLine = playerAccountService.getTagLineByRiotId(riotId);
			} catch (IllegalArgumentException e) {
				// DB에 정보가 없으면 기본값 KR1 사용
				tagLine = "KR1";
			}
		}

		String puuid = riotApiService.getPuuidByRiotId(riotId, tagLine);
		Map<String, Object> summoner = riotApiService.getSummonerByPuuid(puuid);
		String summonerId = (String) summoner.get("id");
		Map<String, Object> rankInfo = riotApiService.getRankedInfo(summonerId);

		return ResponseEntity.ok(rankInfo);
	}

	@GetMapping("/matches/{riotId}")
	public ResponseEntity<List<String>> getMatchesByRiotId(
			@PathVariable String riotId,
			@RequestParam(required = false) String tagLine,
			@RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "20") int count) {

		// tagLine이 없으면 DB에서 조회
		if (tagLine == null || tagLine.isEmpty()) {
			try {
				tagLine = playerAccountService.getTagLineByRiotId(riotId);
			} catch (IllegalArgumentException e) {
				// DB에 정보가 없으면 기본값 KR1 사용
				tagLine = "KR1";
			}
		}

		String puuid = riotApiService.getPuuidByRiotId(riotId, tagLine);
		List<String> matchIds = riotApiService.getRecentMatchIds(puuid, count);

		return ResponseEntity.ok(matchIds);
	}

	@GetMapping("/ranked-matches/{riotId}")
	public ResponseEntity<List<String>> getRankedMatchesByRiotId(
			@PathVariable String riotId,
			@RequestParam(required = false) String tagLine,
			@RequestParam(defaultValue = "20") int count) {

		// tagLine이 없으면 DB에서 조회
		if (tagLine == null || tagLine.isEmpty()) {
			try {
				tagLine = playerAccountService.getTagLineByRiotId(riotId);
			} catch (IllegalArgumentException e) {
				// DB에 정보가 없으면 기본값 KR1 사용
				tagLine = "KR1";
			}
		}

		String puuid = riotApiService.getPuuidByRiotId(riotId, tagLine);
		List<String> matchIds = riotApiService.getRecentSoloRankedMatchIds(puuid, count);

		return ResponseEntity.ok(matchIds);
	}

	@GetMapping("/match/{matchId}")
	public ResponseEntity<Map<String, Object>> getMatchInfo(@PathVariable String matchId) {
		Map<String, Object> matchInfo = riotApiService.getMatchDetail(matchId);
		return ResponseEntity.ok(matchInfo);
	}

	@GetMapping("/ranked-stats/{riotId}")
	public ResponseEntity<Map<String, Object>> getRankedStats(
			@PathVariable String riotId,
			@RequestParam(required = false) String tagLine,
			@RequestParam(defaultValue = "20") int matchCount) {

		// tagLine이 없으면 DB에서 조회
		if (tagLine == null || tagLine.isEmpty()) {
			try {
				tagLine = playerAccountService.getTagLineByRiotId(riotId);
			} catch (IllegalArgumentException e) {
				// DB에 정보가 없으면 기본값 KR1 사용
				tagLine = "KR1";
			}
		}

		Map<String, Object> stats = riotApiService.getSoloRankedStats(riotId, tagLine, matchCount);
		return ResponseEntity.ok(stats);
	}
}