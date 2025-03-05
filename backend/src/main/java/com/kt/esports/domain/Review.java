package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews") // DB 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId; // 리뷰 ID (PK, 자동 증가)

	@ManyToOne
	@JoinColumn(name = "match_id", nullable = false) // FK 설정
	private Match match; // 경기 ID (FK)

	@Column(name = "rating", nullable = false)
	private Integer rating; // 평점 (1~5점)

	@Column(name = "comment", columnDefinition = "TEXT", nullable = false)
	private String comment; // 댓글 내용

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간
}
