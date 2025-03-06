package com.kt.esports.repository;

import com.kt.esports.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMatch_MatchId(Long matchId);
}
