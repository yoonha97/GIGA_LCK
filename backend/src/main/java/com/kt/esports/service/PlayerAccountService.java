package com.kt.esports.service;

import com.kt.esports.domain.Player;
import com.kt.esports.domain.PlayerAccount;
import com.kt.esports.dto.PlayerAccountDTO;
import com.kt.esports.repository.PlayerAccountRepository;
import com.kt.esports.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerAccountService {
	private final PlayerAccountRepository playerAccountRepository;
	private final PlayerRepository playerRepository;

	// 특정 선수의 모든 라이엇 계정 조회
	@Transactional(readOnly = true)
	public List<PlayerAccountDTO> getAccountsByPlayerId(Long playerId) {
		// 선수 존재 여부 확인
		if (!playerRepository.existsById(playerId)) {
			throw new IllegalArgumentException("Player not found with id: " + playerId);
		}

		List<PlayerAccount> accounts = playerAccountRepository.findByPlayer_PlayerId(playerId);
		return accounts.stream()
				.map(PlayerAccountDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 새로운 라이엇 계정 등록
	@Transactional
	public PlayerAccountDTO createPlayerAccount(PlayerAccountDTO playerAccountDTO) {
		Player player = playerRepository.findById(playerAccountDTO.getPlayerId())
				.orElseThrow(() -> new IllegalArgumentException(
						"Player not found with id: " + playerAccountDTO.getPlayerId()));

		// 동일한 라이엇 ID가 있는지 확인
		if (playerAccountRepository.findByRiotId(playerAccountDTO.getRiotId()).isPresent()) {
			throw new IllegalArgumentException("Riot ID already exists: " + playerAccountDTO.getRiotId());
		}

		PlayerAccount playerAccount = PlayerAccount.builder()
				.player(player)
				.riotId(playerAccountDTO.getRiotId())
				.tagLine(playerAccountDTO.getTagLine())
				.build();

		PlayerAccount savedAccount = playerAccountRepository.save(playerAccount);
		return PlayerAccountDTO.fromEntity(savedAccount);
	}

	// 라이엇 계정 정보 수정
	@Transactional
	public PlayerAccountDTO updatePlayerAccount(Long accountId, PlayerAccountDTO playerAccountDTO) {
		PlayerAccount playerAccount = playerAccountRepository.findById(accountId)
				.orElseThrow(() -> new IllegalArgumentException("Player account not found with id: " + accountId));

		// 라이엇 ID 변경 시 중복 확인
		if (!playerAccount.getRiotId().equals(playerAccountDTO.getRiotId())) {
			if (playerAccountRepository.findByRiotId(playerAccountDTO.getRiotId()).isPresent()) {
				throw new IllegalArgumentException("Riot ID already exists: " + playerAccountDTO.getRiotId());
			}
			playerAccount.setRiotId(playerAccountDTO.getRiotId());
		}

		// tagLine 업데이트
		if (playerAccountDTO.getTagLine() != null) {
			playerAccount.setTagLine(playerAccountDTO.getTagLine());
		}

		// 선수 변경 시 존재 여부 확인
		if (!playerAccount.getPlayer().getPlayerId().equals(playerAccountDTO.getPlayerId())) {
			Player newPlayer = playerRepository.findById(playerAccountDTO.getPlayerId())
					.orElseThrow(() -> new IllegalArgumentException(
							"Player not found with id: " + playerAccountDTO.getPlayerId()));
			playerAccount.setPlayer(newPlayer);
		}

		PlayerAccount updatedAccount = playerAccountRepository.save(playerAccount);
		return PlayerAccountDTO.fromEntity(updatedAccount);
	}

	// 라이엇 계정 삭제
	@Transactional
	public void deletePlayerAccount(Long accountId) {
		if (!playerAccountRepository.existsById(accountId)) {
			throw new IllegalArgumentException("Player account not found with id: " + accountId);
		}
		playerAccountRepository.deleteById(accountId);
	}

	// 모든 라이엇 계정 조회
	@Transactional(readOnly = true)
	public List<PlayerAccountDTO> getAllPlayerAccounts() {
		List<PlayerAccount> accounts = playerAccountRepository.findAll();
		return accounts.stream()
				.map(PlayerAccountDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 라이엇 ID로 계정 조회
	@Transactional(readOnly = true)
	public PlayerAccountDTO getAccountByRiotId(String riotId) {
		PlayerAccount account = playerAccountRepository.findByRiotId(riotId)
				.orElseThrow(() -> new IllegalArgumentException("Player account not found with riot id: " + riotId));

		return PlayerAccountDTO.fromEntity(account);
	}

	// 라이엇 ID로 tagLine 조회
	@Transactional(readOnly = true)
	public String getTagLineByRiotId(String riotId) {
		PlayerAccount account = playerAccountRepository.findByRiotId(riotId)
				.orElseThrow(() -> new IllegalArgumentException("Player account not found with riot id: " + riotId));

		return account.getTagLine();
	}
}