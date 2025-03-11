package com.kt.esports.service;

import com.kt.esports.domain.Match;
import com.kt.esports.domain.Team;
import com.kt.esports.dto.MatchDTO;
import com.kt.esports.dto.PlayerDTO;
import com.kt.esports.dto.TeamDTO;
import com.kt.esports.repository.MatchRepository;
import com.kt.esports.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamService {

	private final TeamRepository teamRepository;
	private final PlayerService playerService;
	private final MatchRepository matchRepository;
	private final MatchService matchService;

	@Autowired
	public TeamService(TeamRepository teamRepository, PlayerService playerService,
			RiotApiService riotApiService, PlayerAccountService playerAccountService,
			MatchRepository matchRepository, MatchService matchService) {
		this.teamRepository = teamRepository;
		this.playerService = playerService;
		this.matchRepository = matchRepository;
		this.matchService = matchService;
	}

	// 모든 팀 조회
	public List<TeamDTO> getAllTeams() {
		return teamRepository.findAll().stream()
				.map(TeamDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// ID로 팀 조회
	public TeamDTO getTeamById(Long teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new NoSuchElementException("해당 ID의 팀을 찾을 수 없습니다: " + teamId));
		return TeamDTO.fromEntity(team);
	}

	// 팀 이름으로 팀 조회
	public TeamDTO getTeamByName(String teamName) {
		Team team = teamRepository.findByTeamName(teamName)
				.orElseThrow(() -> new NoSuchElementException("해당 이름의 팀을 찾을 수 없습니다: " + teamName));
		return TeamDTO.fromEntity(team);
	}

	// 팀 소속 선수 목록 조회
	@Transactional(readOnly = true)
	public List<PlayerDTO> getTeamPlayers(Long teamId) {
		// 팀이 존재하는지 먼저 확인
		if (!teamRepository.existsById(teamId)) {
			throw new NoSuchElementException("해당 ID의 팀을 찾을 수 없습니다: " + teamId);
		}

		// PlayerService의 기존 메소드를 활용
		return playerService.getPlayersByTeamId(teamId);
	}

	// 팀의 최근 공식 경기 10건 조회
	@Transactional(readOnly = true)
	public List<MatchDTO> getTeamRecentMatches(Long teamId) {
		// 팀이 존재하는지 먼저 확인
		if (!teamRepository.existsById(teamId)) {
			throw new NoSuchElementException("해당 ID의 팀을 찾을 수 없습니다: " + teamId);
		}

		// 최근 경기순으로 정렬하여 최대 10개까지 조회
		List<Match> matches = matchRepository.findByHomeTeam_TeamIdOrAwayTeam_TeamId(teamId, teamId).stream()
				.sorted(Comparator.comparing(Match::getDate).reversed())
				.limit(10)
				.collect(Collectors.toList());

		// MatchDTO로 변환하여 반환
		return matches.stream()
				.map(match -> {
					try {
						return matchService.getMatchById(match.getMatchId());
					} catch (Exception e) {
						// 경기 정보 변환 중 오류 발생 시 null 반환 (나중에 필터링됨)
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	// 팀 이름으로 해당 팀의 최근 10경기를 조회
	@Transactional(readOnly = true)
	public List<MatchDTO> getTeamRecentMatchesByName(String teamName) {
		// 팀 이름으로 팀 조회
		TeamDTO team = getTeamByName(teamName);
		// ID로 최근 경기 조회 메소드 호출
		return getTeamRecentMatches(team.getTeamId());
	}
}