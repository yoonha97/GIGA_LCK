package com.kt.esports.service;

import com.kt.esports.dto.MatchDTO;
import com.kt.esports.domain.Match;
import com.kt.esports.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 (성능 최적화)
public class MatchService {

	private final MatchRepository matchRepository;
	private final YouTubeService youTubeService;

	// 전체 경기 목록 조회
	public List<MatchDTO> getAllMatches() {
		List<Match> matches = matchRepository.findAll();
		if (matches.isEmpty()) {
			return List.of(); // 빈 리스트 반환
		}
		return matches.stream()
				.map(match -> MatchDTO.fromEntity(match, youTubeService.getReplayLink(
						match.getHomeTeam(), match.getAwayTeam(), match.getDate())))
				.collect(Collectors.toList());
	}

	// 월별 경기 목록 조회
	public List<MatchDTO> getMatchesByMonth(Integer year, Integer month) {
		// year, month 값이 없으면 가장 최신 경기 날짜를 가져와서 기본값으로 사용
		if (year == null || month == null) {
			Match latestMatch = matchRepository.findTopByOrderByDateDesc();
			if (latestMatch != null) {
				year = latestMatch.getDate().getYear();
				month = latestMatch.getDate().getMonthValue();
			} else {
				// 경기 데이터가 하나도 없을 경우 기본값 설정 (2025년 2월로 설정)
				year = 2025;
				month = 2;
			}
		}

		LocalDate startDate = YearMonth.of(year, month).atDay(1);
		LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

		List<Match> matches = matchRepository.findByDateBetween(startDate, endDate);
		if (matches.isEmpty()) {
			return List.of(); // 빈 리스트 반환
		}

		return matches.stream()
				.map(match -> MatchDTO.fromEntity(match, youTubeService.getReplayLink(
						match.getHomeTeam(), match.getAwayTeam(), match.getDate())))
				.collect(Collectors.toList());
	}

	// 팀별 경기 목록 조회
	public List<MatchDTO> getMatchesByTeam(String teamName) {
		List<Match> matches = matchRepository.findByHomeTeamOrAwayTeam(teamName, teamName);
		if (matches.isEmpty()) {
			return List.of(); // 빈 리스트 반환
		}

		return matches.stream()
				.map(match -> MatchDTO.fromEntity(match, youTubeService.getReplayLink(
						match.getHomeTeam(), match.getAwayTeam(), match.getDate())))
				.collect(Collectors.toList());
	}

	// 특정 경기 조회 (matchId 기반)
	public Match getMatchById(Long matchId) {
		return matchRepository.findById(matchId)
				.orElseThrow(() -> new IllegalArgumentException("해당 경기 ID가 없습니다: " + matchId));
	}
}
