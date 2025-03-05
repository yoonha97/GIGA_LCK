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

	private String stage; // 스테이지
	private String homeTeam; // 홈팀
	private String awayTeam; // 원정팀
	private LocalDate date; // 날짜
	private String time; // 시간
	private String score; // 점수
}
