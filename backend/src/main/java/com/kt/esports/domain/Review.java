package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reviews")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 자동 증가
	private Long reviewId;

	@ManyToOne(fetch = FetchType.LAZY) // N:1 관계
	@JoinColumn(name = "match_id", nullable = false)
	private Match match;

	private Integer rating; // 평점
	private String comment; // 댓글
	private LocalDateTime createdAt; // 생성일시
}
