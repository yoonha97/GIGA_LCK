package com.kt.esports.repository;

import com.kt.esports.domain.Review;
import com.kt.esports.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMatch(Match match); // 특정 경기의 리뷰 찾기
}
