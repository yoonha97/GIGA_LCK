package com.kt.esports.controller;

import com.kt.esports.dto.PlayerDTO;
import com.kt.esports.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
	private final PlayerService playerService;

	// 모든 선수 목록 조회
	@GetMapping
	public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
		List<PlayerDTO> players = playerService.getAllPlayers();
		return ResponseEntity.ok(players);
	}

	// 선수 상세 정보 조회
	@GetMapping("/{playerId}")
	public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long playerId) {
		PlayerDTO player = playerService.getPlayerById(playerId);
		return ResponseEntity.ok(player);
	}

	// 새로운 선수 등록
	@PostMapping
	public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
		PlayerDTO createdPlayer = playerService.createPlayer(playerDTO);
		return ResponseEntity.ok(createdPlayer);
	}

	// 선수 정보 수정
	@PutMapping("/{playerId}")
	public ResponseEntity<PlayerDTO> updatePlayer(
			@PathVariable Long playerId,
			@RequestBody PlayerDTO playerDTO) {
		PlayerDTO updatedPlayer = playerService.updatePlayer(playerId, playerDTO);
		return ResponseEntity.ok(updatedPlayer);
	}

	// 선수 삭제
	@DeleteMapping("/{playerId}")
	public ResponseEntity<Void> deletePlayer(@PathVariable Long playerId) {
		playerService.deletePlayer(playerId);
		return ResponseEntity.noContent().build();
	}

	// 포지션별 선수 목록 조회
	@GetMapping("/position/{position}")
	public ResponseEntity<List<PlayerDTO>> getPlayersByPosition(@PathVariable String position) {
		List<PlayerDTO> players = playerService.getPlayersByPosition(position);
		return ResponseEntity.ok(players);
	}
}