package com.kt.esports.repository;

import com.kt.esports.domain.Player;
import com.kt.esports.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
	// 팀으로 선수 조회
	List<Player> findByTeam(Team team);
}
