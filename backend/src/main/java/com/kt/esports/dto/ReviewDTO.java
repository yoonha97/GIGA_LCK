package com.kt.esports.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
	private Long reviewId;
	private String matchId;
	private Integer rating;
	private String comment;
	private LocalDateTime createdAt;

	public ReviewDTO(Long reviewId, String matchId, Integer rating, String comment, LocalDateTime createdAt) {
		this.reviewId = reviewId;
		this.matchId = matchId;
		this.rating = rating;
		this.comment = comment;
		this.createdAt = createdAt;
	}
}
