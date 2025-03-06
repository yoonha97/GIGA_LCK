package com.kt.esports.repository;

import com.kt.esports.domain.Review;
import com.kt.esports.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	// 특정 경기(matchId)의 리뷰 리스트 조회
	List<Review> findByMatch(Match match);
}
