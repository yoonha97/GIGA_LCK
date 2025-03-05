package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches") // DB 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

	@Id
	@Column(name = "match_id", length = 255)
	private String matchId; // 경기 ID (PK)

	@Column(name = "team1", length = 100, nullable = false)
	private String team1; // 팀 1 이름

	@Column(name = "team2", length = 255, nullable = false)
	private String team2; // 팀 2 이름

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime; // 경기 시작 시간

	@Column(name = "winner", length = 100)
	private String winner; // 승리한 팀 (NULL 가능)

	// 리뷰(Comments)와 1:N 관계 설정
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews;
}
