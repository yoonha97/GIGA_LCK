package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Matches")
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long matchId;

	@Column(nullable = false)
	private String stage; // 스테이지

	@ManyToOne
	@JoinColumn(name = "home_team_id", nullable = false)
	private Team homeTeam; // 홈팀

	@ManyToOne
	@JoinColumn(name = "away_team_id", nullable = false)
	private Team awayTeam; // 원정팀

	@Column(nullable = false)
	private LocalDate date; // 경기 날짜

	@Column(nullable = false)
	private String time; // 경기 시간

	private String score; // 경기 결과 (예: "2-1")

}
