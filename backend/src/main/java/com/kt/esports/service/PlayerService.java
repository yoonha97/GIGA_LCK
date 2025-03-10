package com.kt.esports.service;

import com.kt.esports.domain.Player;
import com.kt.esports.domain.Team;
import com.kt.esports.dto.PlayerDTO;
import com.kt.esports.repository.PlayerRepository;
import com.kt.esports.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;

	// 모든 선수 목록 조회
	@Transactional(readOnly = true)
	public List<PlayerDTO> getAllPlayers() {
		List<Player> players = playerRepository.findAll();
		return players.stream()
				.map(PlayerDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 선수 ID로 선수 상세 정보 조회
	@Transactional(readOnly = true)
	public PlayerDTO getPlayerById(Long playerId) {
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + playerId));
		return PlayerDTO.fromEntity(player);
	}

	// 팀별 선수 목록 조회
	@Transactional(readOnly = true)
	public List<PlayerDTO> getPlayersByTeamId(Long teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));

		List<Player> players = playerRepository.findByTeam(team);
		return players.stream()
				.map(PlayerDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 포지션별 선수 목록 조회
	@Transactional(readOnly = true)
	public List<PlayerDTO> getPlayersByPosition(String position) {
		List<Player> players = playerRepository.findByPosition(position);
		return players.stream()
				.map(PlayerDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 새로운 선수 등록
	@Transactional
	public PlayerDTO createPlayer(PlayerDTO playerDTO) {
		Player player = new Player();
		player.setPlayerName(playerDTO.getPlayerName());
		player.setPosition(playerDTO.getPosition());
		player.setDescription(playerDTO.getDescription());

		// 팀 설정 (FA 선수인 경우 null)
		if (playerDTO.getTeamId() != null) {
			Team team = teamRepository.findById(playerDTO.getTeamId())
					.orElseThrow(
							() -> new IllegalArgumentException("Team not found with id: " + playerDTO.getTeamId()));
			player.setTeam(team);
		}

		Player savedPlayer = playerRepository.save(player);
		return PlayerDTO.fromEntity(savedPlayer);
	}

	// 선수 정보 수정
	@Transactional
	public PlayerDTO updatePlayer(Long playerId, PlayerDTO playerDTO) {
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + playerId));

		player.setPlayerName(playerDTO.getPlayerName());
		player.setPosition(playerDTO.getPosition());
		player.setDescription(playerDTO.getDescription());

		// 팀 변경 처리 (FA 처리 포함)
		if (playerDTO.getTeamId() != null) {
			Team team = teamRepository.findById(playerDTO.getTeamId())
					.orElseThrow(
							() -> new IllegalArgumentException("Team not found with id: " + playerDTO.getTeamId()));
			player.setTeam(team);
		} else {
			player.setTeam(null); // FA 처리
		}

		Player updatedPlayer = playerRepository.save(player);
		return PlayerDTO.fromEntity(updatedPlayer);
	}

	// 선수 삭제
	@Transactional
	public void deletePlayer(Long playerId) {
		if (!playerRepository.existsById(playerId)) {
			throw new IllegalArgumentException("Player not found with id: " + playerId);
		}
		playerRepository.deleteById(playerId);
	}
}