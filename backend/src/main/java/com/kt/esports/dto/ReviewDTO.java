package com.kt.esports.dto;

import com.kt.esports.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;
    private Long matchId; // 경기 ID
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    // Entity → DTO 변환 메서드
    public static ReviewDTO fromEntity(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .matchId(review.getMatch().getMatchId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
