package com.kt.esports.controller;

import com.kt.esports.dto.PlayerAccountDTO;
import com.kt.esports.service.PlayerAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlayerAccountController {
	private final PlayerAccountService playerAccountService;

	// 모든 라이엇 계정 조회
	@GetMapping("/player-accounts")
	public ResponseEntity<List<PlayerAccountDTO>> getAllPlayerAccounts() {
		List<PlayerAccountDTO> accounts = playerAccountService.getAllPlayerAccounts();
		return ResponseEntity.ok(accounts);
	}

	// 특정 선수의 라이엇 계정 목록 조회
	@GetMapping("/players/{playerId}/accounts")
	public ResponseEntity<List<PlayerAccountDTO>> getPlayerAccounts(@PathVariable Long playerId) {
		List<PlayerAccountDTO> accounts = playerAccountService.getAccountsByPlayerId(playerId);
		return ResponseEntity.ok(accounts);
	}

	// 라이엇 ID로 계정 조회
	@GetMapping("/player-accounts/riot-id/{riotId}")
	public ResponseEntity<PlayerAccountDTO> getAccountByRiotId(@PathVariable String riotId) {
		PlayerAccountDTO account = playerAccountService.getAccountByRiotId(riotId);
		return ResponseEntity.ok(account);
	}

	// 새로운 라이엇 계정 등록
	@PostMapping("/player-accounts")
	public ResponseEntity<PlayerAccountDTO> createPlayerAccount(@RequestBody PlayerAccountDTO playerAccountDTO) {
		PlayerAccountDTO createdAccount = playerAccountService.createPlayerAccount(playerAccountDTO);
		return ResponseEntity.ok(createdAccount);
	}

	// 라이엇 계정 정보 수정
	@PutMapping("/player-accounts/{accountId}")
	public ResponseEntity<PlayerAccountDTO> updatePlayerAccount(
			@PathVariable Long accountId,
			@RequestBody PlayerAccountDTO playerAccountDTO) {
		PlayerAccountDTO updatedAccount = playerAccountService.updatePlayerAccount(accountId, playerAccountDTO);
		return ResponseEntity.ok(updatedAccount);
	}

	// 라이엇 계정 삭제
	@DeleteMapping("/player-accounts/{accountId}")
	public ResponseEntity<Void> deletePlayerAccount(@PathVariable Long accountId) {
		playerAccountService.deletePlayerAccount(accountId);
		return ResponseEntity.noContent().build();
	}
}