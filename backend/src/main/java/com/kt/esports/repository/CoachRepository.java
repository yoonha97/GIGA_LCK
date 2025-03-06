package com.kt.esports.repository;

import com.kt.esports.domain.Coach;
import com.kt.esports.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
	// 팀으로 코치 조회
	List<Coach> findByTeam(Team team);
}
