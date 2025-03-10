package com.kt.esports.service;

import com.kt.esports.domain.Team;
import com.kt.esports.dto.PlayerDTO;
import com.kt.esports.dto.TeamDTO;
import com.kt.esports.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TeamService {

	private final TeamRepository teamRepository;
	private final PlayerService playerService;

	@Autowired
	public TeamService(TeamRepository teamRepository, PlayerService playerService) {
		this.teamRepository = teamRepository;
		this.playerService = playerService;
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
}