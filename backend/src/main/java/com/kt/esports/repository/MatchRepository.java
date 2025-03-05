package com.kt.esports.repository;

import com.kt.esports.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findByDate(String date); // 특정 날짜의 경기 찾기
}
