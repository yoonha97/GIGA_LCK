package com.kt.esports.repository;

import com.kt.esports.domain.Player;
import com.kt.esports.domain.PlayerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Long> {
	// 특정 선수의 모든 라이엇 계정 조회
	List<PlayerAccount> findByPlayer(Player player);

	// 특정 선수 ID로 모든 라이엇 계정 조회
	List<PlayerAccount> findByPlayer_PlayerId(Long playerId);

	// 라이엇 ID로 계정 조회
	Optional<PlayerAccount> findByRiotId(String riotId);
}