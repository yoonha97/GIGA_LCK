package com.kt.esports.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MatchDTO {
	private String matchId;
	private String team1;
	private String team2;
	private LocalDateTime startTime;
	private String winner;
	private List<ReviewDTO> reviews;

	public MatchDTO(String matchId, String team1, String team2, LocalDateTime startTime, String winner,
			List<ReviewDTO> reviews) {
		this.matchId = matchId;
		this.team1 = team1;
		this.team2 = team2;
		this.startTime = startTime;
		this.winner = winner;
		this.reviews = reviews;
	}
}
