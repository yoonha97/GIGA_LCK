package com.kt.esports.service;

import com.kt.esports.domain.Match;
import com.kt.esports.domain.Review;
import com.kt.esports.dto.ReviewDTO;
import com.kt.esports.repository.MatchRepository;
import com.kt.esports.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final MatchRepository matchRepository;

	// 특정 경기(matchId)의 리뷰 리스트 조회
	public List<ReviewDTO> getReviewsByMatch(Long matchId) {
		List<Review> reviews = reviewRepository.findByMatch_MatchId(matchId);
		return reviews.stream().map(ReviewDTO::fromEntity).collect(Collectors.toList());
	}

	// 새로운 리뷰 저장
	@Transactional
	public ReviewDTO createReview(Long matchId, ReviewDTO reviewDTO) {
		Match match = matchRepository.findById(matchId)
				.orElseThrow(() -> new IllegalArgumentException("해당 경기 ID가 없습니다: " + matchId));

		Review review = Review.builder()
				.match(match)
				.rating(reviewDTO.getRating())
				.comment(reviewDTO.getComment())
				.createdAt(LocalDateTime.now())
				.build();

		Review savedReview = reviewRepository.save(review);
		return ReviewDTO.fromEntity(savedReview);
	}

	// 리뷰 수정
	@Transactional
	public ReviewDTO updateReview(Long matchId, Long reviewId, ReviewDTO reviewDTO) {
		Match match = matchRepository.findById(matchId)
				.orElseThrow(() -> new IllegalArgumentException("해당 경기 ID가 없습니다: " + matchId));

		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("해당 리뷰 ID가 없습니다: " + reviewId));

		if (!review.getMatch().equals(match)) {
			throw new IllegalArgumentException("해당 경기의 리뷰가 아닙니다.");
		}

		// 기존 객체의 필드 값 수정 (새로운 객체 생성 X)
		review.setRating(reviewDTO.getRating());
		review.setComment(reviewDTO.getComment());

		return ReviewDTO.fromEntity(review);
	}

	// 리뷰 삭제
	@Transactional
	public void deleteReview(Long matchId, Long reviewId) {
		Match match = matchRepository.findById(matchId)
				.orElseThrow(() -> new IllegalArgumentException("해당 경기 ID가 없습니다: " + matchId));

		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("해당 리뷰 ID가 없습니다: " + reviewId));

		if (!review.getMatch().equals(match)) {
			throw new IllegalArgumentException("해당 경기의 리뷰가 아닙니다.");
		}

		reviewRepository.delete(review);
	}
}
