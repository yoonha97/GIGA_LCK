package com.kt.esports.repository;

import com.kt.esports.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMatch_MatchId(String matchId); // 특정 경기(match_id)의 리뷰 찾기
}
