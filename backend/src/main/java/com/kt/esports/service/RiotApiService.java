package com.kt.esports.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiotApiService {

	@Value("${riot.api.key}")
	private String apiKey;

	private final WebClient riotAsiaWebClient;
	private final WebClient riotKrWebClient;
	// 솔로랭크 큐 ID
	private final int RANKED_SOLO_QUEUE_ID = 420;

	public RiotApiService(WebClient riotAsiaWebClient, WebClient riotKrWebClient) {
		this.riotAsiaWebClient = riotAsiaWebClient;
		this.riotKrWebClient = riotKrWebClient;
	}

	// 라이엇 ID (gameName + tagLine)로 PUUID 조회
	public String getPuuidByRiotId(String gameName, String tagLine) {
		String path = "/riot/account/v1/accounts/by-riot-id/"
				+ UriUtils.encode(gameName, "UTF-8") + "/" + tagLine;

		System.out.println("------------DEBUG-----------");
		System.out.println("Calling Riot API with gameName: " + gameName + ", tagLine: " + tagLine);
		System.out.println("Full path: " + path);
		System.out.println("----------------------------");

		Map response = riotAsiaWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(Map.class)
				.block();

		return (String) response.get("puuid");
	}

	// PUUID로 최근 매치 ID 목록 조회 (count: 가져올 매치 수)
	public List<String> getRecentMatchIds(String puuid, int count) {
		String path = "/lol/match/v5/matches/by-puuid/" + puuid
				+ "/ids?start=0&count=" + count;

		return riotAsiaWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(List.class)
				.block();
	}

	// PUUID로 최근 솔로 랭크 매치 ID 목록 조회
	public List<String> getRecentSoloRankedMatchIds(String puuid, int count) {
		String path = "/lol/match/v5/matches/by-puuid/" + puuid
				+ "/ids?queue=" + RANKED_SOLO_QUEUE_ID + "&start=0&count=" + count;

		return riotAsiaWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(List.class)
				.block();
	}

	// 매치 ID로 매치 상세 정보 조회
	public Map<String, Object> getMatchDetail(String matchId) {
		String path = "/lol/match/v5/matches/" + matchId;

		return riotAsiaWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(Map.class)
				.block();
	}

	// 유저의 솔랭 전적 통계 가져오기
	public Map<String, Object> getSoloRankedStats(String gameName, String tagLine, int matchCount) {
		String puuid = getPuuidByRiotId(gameName, tagLine);
		List<String> matchIds = getRecentSoloRankedMatchIds(puuid, matchCount);

		Map<String, Object> stats = new HashMap<>();
		stats.put("totalGames", matchIds.size());

		int wins = 0;
		int kills = 0;
		int deaths = 0;
		int assists = 0;

		for (String matchId : matchIds) {
			Map<String, Object> matchDetail = getMatchDetail(matchId);
			Map<String, Object> info = (Map<String, Object>) matchDetail.get("info");
			List<Map<String, Object>> participants = (List<Map<String, Object>>) info.get("participants");

			for (Map<String, Object> participant : participants) {
				String participantPuuid = (String) participant.get("puuid");
				if (participantPuuid.equals(puuid)) {
					if ((boolean) participant.get("win")) {
						wins++;
					}
					kills += ((Number) participant.get("kills")).intValue();
					deaths += ((Number) participant.get("deaths")).intValue();
					assists += ((Number) participant.get("assists")).intValue();
					break;
				}
			}
		}

		stats.put("wins", wins);
		stats.put("losses", matchIds.size() - wins);
		stats.put("winRate", matchIds.isEmpty() ? 0 : (double) wins / matchIds.size() * 100);
		stats.put("kills", kills);
		stats.put("deaths", deaths);
		stats.put("assists", assists);
		stats.put("kda", deaths == 0 ? (kills + assists) : (double) (kills + assists) / deaths);

		// 소환사 정보 및 티어 추가
		Map<String, Object> summonerInfo = getSummonerByPuuid(puuid);
		stats.put("summonerInfo", summonerInfo);

		String summonerId = (String) summonerInfo.get("id");
		Map<String, Object> rankInfo = getRankedInfo(summonerId);
		stats.put("rankInfo", rankInfo);

		return stats;
	}

	// PUUID로 소환사 정보 조회
	public Map<String, Object> getSummonerByPuuid(String puuid) {
		String path = "/lol/summoner/v4/summoners/by-puuid/" + puuid;

		return riotKrWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(Map.class)
				.block();
	}

	// 소환사 ID로 랭크 정보 조회
	public Map<String, Object> getRankedInfo(String summonerId) {
		String path = "/lol/league/v4/entries/by-summoner/" + summonerId;

		List<Map<String, Object>> leagueEntries = riotKrWebClient.get()
				.uri(path)
				.headers(this::addRiotHeaders)
				.retrieve()
				.bodyToMono(List.class)
				.block();

		// 솔로 랭크 정보 찾기
		for (Map<String, Object> entry : leagueEntries) {
			if ("RANKED_SOLO_5x5".equals(entry.get("queueType"))) {
				return entry;
			}
		}

		return new HashMap<>();
	}

	// HTTP 헤더 추가
	private void addRiotHeaders(HttpHeaders headers) {
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
		headers.add("Accept-Language", "ko-KR,ko;q=0.9");
		headers.add("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.add("Origin", "https://developer.riotgames.com");
		headers.add("X-Riot-Token", apiKey);
	}
}