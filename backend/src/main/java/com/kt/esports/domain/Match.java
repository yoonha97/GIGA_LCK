package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "matches")
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL 자동 증가
	private Long matchId;

	@Column(nullable = false)
	private String stage; // 스테이지

	@Column(nullable = false)
	private String homeTeam; // 홈팀

	@Column(nullable = false)
	private String awayTeam; // 원정팀

	@Column(nullable = false)
	private LocalDate date; // 경기 날짜

	@Column(nullable = false)
	private String time; // 경기 시간

	private String score; // 경기 결과 (예: "2-1")

}
