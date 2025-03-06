package com.kt.esports.controller;

import com.kt.esports.dto.ReviewDTO;
import com.kt.esports.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches/{matchId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// 특정 경기의 리뷰 조회
	@GetMapping
	public ResponseEntity<List<ReviewDTO>> getReviewsByMatch(@PathVariable Long matchId) {
		List<ReviewDTO> reviews = reviewService.getReviewsByMatch(matchId);
		return ResponseEntity.ok(reviews);
	}

	// 특정 경기에 리뷰 등록
	@PostMapping
	public ResponseEntity<ReviewDTO> createReview(
			@PathVariable Long matchId,
			@RequestBody ReviewDTO reviewDTO) {

		ReviewDTO savedReview = reviewService.createReview(matchId, reviewDTO);
		return ResponseEntity.ok(savedReview);
	}

	// 특정 경기의 리뷰 수정
	@PatchMapping("/{reviewId}")
	public ResponseEntity<ReviewDTO> updateReview(
			@PathVariable Long matchId,
			@PathVariable Long reviewId,
			@RequestBody ReviewDTO reviewDTO) {

		ReviewDTO updatedReview = reviewService.updateReview(matchId, reviewId, reviewDTO);
		return ResponseEntity.ok(updatedReview);
	}

	// 특정 경기의 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteReview(
			@PathVariable Long matchId,
			@PathVariable Long reviewId) {

		reviewService.deleteReview(matchId, reviewId);
		return ResponseEntity.noContent().build();
	}
}
