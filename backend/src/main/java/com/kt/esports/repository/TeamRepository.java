package com.kt.esports.repository;

import com.kt.esports.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	// 팀 이름으로 팀 조회
	Optional<Team> findByTeamName(String teamName);
}
