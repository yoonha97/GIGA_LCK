package com.kt.esports.repository;

import com.kt.esports.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

	// 특정 기간(월) 동안의 경기 조회
	List<Match> findByDateBetween(LocalDate startDate, LocalDate endDate);

	// 특정 팀이 포함된 경기 조회 (홈팀 OR 원정팀)
	List<Match> findByHomeTeamOrAwayTeam(String homeTeam, String awayTeam);

	// 가장 최근 경기 날짜 찾기
	Match findTopByOrderByDateDesc();
}
