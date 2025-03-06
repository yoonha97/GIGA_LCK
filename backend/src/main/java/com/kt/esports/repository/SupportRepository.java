package com.kt.esports.repository;

import com.kt.esports.domain.Support;
import com.kt.esports.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
	// 팀으로 응원댓글 조회
	List<Support> findByTeam(Team team);
}
