package com.kt.esports.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameAnalysisService {

	private final RiotApiService riotApiService;
	private final WebClient openaiWebClient;

	public GameAnalysisService(RiotApiService riotApiService, WebClient openaiWebClient) {
		this.riotApiService = riotApiService;
		this.openaiWebClient = openaiWebClient;
	}

	public String analyzePlayerPerformance(String gameName, String tagLine, int matchCount) {
		// 1. 라이엇 API에서 데이터 가져오기
		Map<String, Object> stats = riotApiService.getSoloRankedStats(gameName, tagLine, matchCount);

		// 최근 매치 목록 가져오기
		String puuid = riotApiService.getPuuidByRiotId(gameName, tagLine);
		List<String> matchIds = riotApiService.getRecentSoloRankedMatchIds(puuid, matchCount);

		// 2. 분석을 위한 데이터 가공
		StringBuilder matchDetails = new StringBuilder();
		int matchNum = 1;

		for (String matchId : matchIds) {
			Map<String, Object> matchDetail = riotApiService.getMatchDetail(matchId);
			Map<String, Object> info = (Map<String, Object>) matchDetail.get("info");
			List<Map<String, Object>> participants = (List<Map<String, Object>>) info.get("participants");

			for (Map<String, Object> participant : participants) {
				String participantPuuid = (String) participant.get("puuid");
				if (participantPuuid.equals(puuid)) {
					String championName = (String) participant.get("championName");
					boolean win = (boolean) participant.get("win");
					int kills = ((Number) participant.get("kills")).intValue();
					int deaths = ((Number) participant.get("deaths")).intValue();
					int assists = ((Number) participant.get("assists")).intValue();

					matchDetails.append("Game ").append(matchNum).append(": ")
							.append("Champion: ").append(championName).append(", ")
							.append("Result: ").append(win ? "Win" : "Loss").append(", ")
							.append("KDA: ").append(kills).append("/").append(deaths)
							.append("/").append(assists).append("\n");

					break;
				}
			}
			matchNum++;
		}

		// 3. OpenAI에 분석 요청
		String systemPrompt = "당신은 리그 오브 레전드 전문 분석가입니다. 플레이어의 최근 경기 데이터를 분석하고 인사이트를 제공하세요. 한국어로 응답해 주세요.";

		String userPrompt = String.format("""
				다음 리그 오브 레전드 플레이어의 통계와 최근 경기를 분석해 주세요:

				플레이어: %s#%s

				통계 요약:
				- 총 게임 수: %d
				- 승리: %d
				- 패배: %d
				- 승률: %.2f%%
				- KDA: %.2f

				최근 경기 상세:
				%s

				다음 내용을 포함해 주세요:
				1. 플레이어의 전반적인 성과 평가
				2. 데이터에 기반한 장점과 약점
				3. 향상을 위한 제안
				4. 선호하는 챔피언과 그 효율성
				5. 플레이 스타일에서 주목할만한 패턴

				간결하면서도 통찰력 있는 분석을 제공해 주세요.
				""",
				gameName, tagLine,
				stats.get("totalGames"),
				stats.get("wins"),
				stats.get("losses"),
				stats.get("winRate"),
				stats.get("kda"),
				matchDetails.toString());

		try {
			return callOpenAI(systemPrompt, userPrompt);
		} catch (Exception e) {
			return "플레이어 성능 분석 중 오류가 발생했습니다: " + e.getMessage();
		}
	}

	public String analyzeMatchDetail(String matchId, String gameName, String tagLine) {
		// 1. 라이엇 API에서 매치 상세정보와 플레이어 PUUID 가져오기
		Map<String, Object> matchDetail = riotApiService.getMatchDetail(matchId);
		String puuid = riotApiService.getPuuidByRiotId(gameName, tagLine);

		// 2. 매치 데이터 가공
		Map<String, Object> info = (Map<String, Object>) matchDetail.get("info");
		long gameCreation = ((Number) info.get("gameCreation")).longValue();
		int gameDuration = ((Number) info.get("gameDuration")).intValue();
		String gameMode = (String) info.get("gameMode");

		List<Map<String, Object>> participants = (List<Map<String, Object>>) info.get("participants");

		// 플레이어 데이터 가공
		Map<String, Object> playerData = null;
		for (Map<String, Object> participant : participants) {
			if (puuid.equals(participant.get("puuid"))) {
				playerData = participant;
				break;
			}
		}

		if (playerData == null) {
			return "해당 매치에서 플레이어를 찾을 수 없습니다.";
		}

		// 3. OpenAI에 분석 요청
		String systemPrompt = "당신은 리그 오브 레전드 전문 분석가입니다. 특정 경기의 상세 내용을 분석하고 인사이트를 제공하세요. 한국어로 응답해 주세요.";

		String userPrompt = String.format("""
				다음 리그 오브 레전드 경기에서 플레이어 %s#%s의 성과를 분석해 주세요:

				매치 ID: %s
				게임 모드: %s
				게임 시간: %d분

				플레이어 성과:
				- 챔피언: %s
				- 결과: %s
				- 포지션: %s
				- KDA: %d/%d/%d
				- 시야 점수: %d
				- CS: %d (분당 %.1f)
				- 가한 피해량: %d
				- 받은 피해량: %d

				주요 통계:
				- 첫 킬 획득: %s
				- 목표물 스틸: %d
				- 와드 설치: %d
				- 와드 제거: %d

				다음 내용을 포함해 주세요:
				1. 이 경기에서 플레이어의 성과 평가
				2. 주요 순간과 기여도
				3. 개선할 수 있었던 부분
				4. 역할/챔피언 플레이의 적절성
				5. 이 챔피언으로 향후 게임을 위한 조언

				이 통계를 바탕으로 통찰력 있는 분석을 제공해 주세요.
				""",
				gameName, tagLine,
				matchId,
				gameMode,
				gameDuration / 60,
				playerData.get("championName"),
				((boolean) playerData.get("win")) ? "승리" : "패배",
				playerData.get("lane"),
				((Number) playerData.get("kills")).intValue(),
				((Number) playerData.get("deaths")).intValue(),
				((Number) playerData.get("assists")).intValue(),
				((Number) playerData.get("visionScore")).intValue(),
				((Number) playerData.get("totalMinionsKilled")).intValue(),
				((Number) playerData.get("totalMinionsKilled")).floatValue() / (gameDuration / 60f),
				((Number) playerData.get("totalDamageDealtToChampions")).intValue(),
				((Number) playerData.get("totalDamageTaken")).intValue(),
				((boolean) playerData.getOrDefault("firstBloodKill", false)) ? "예" : "아니오",
				((Number) playerData.getOrDefault("objectivesStolen", 0)).intValue(),
				((Number) playerData.get("wardsPlaced")).intValue(),
				((Number) playerData.get("wardsKilled")).intValue());

		try {
			return callOpenAI(systemPrompt, userPrompt);
		} catch (Exception e) {
			return "매치 상세 분석 중 오류가 발생했습니다: " + e.getMessage();
		}
	}

	private String callOpenAI(String systemPrompt, String userPrompt) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "gpt-4o");
		requestBody.put("max_tokens", 800);

		List<Map<String, String>> messages = new ArrayList<>();
		messages.add(Map.of("role", "system", "content", systemPrompt));
		messages.add(Map.of("role", "user", "content", userPrompt));
		requestBody.put("messages", messages);

		Map<String, Object> response = openaiWebClient.post()
				.uri("/chat/completions")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(Map.class)
				.block();

		List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
		Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
		return (String) message.get("content");
	}
}