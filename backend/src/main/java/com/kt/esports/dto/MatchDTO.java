package com.kt.esports.dto;

import com.kt.esports.domain.Match;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {
	private Long matchId;
	private String stage;
	private String homeTeam;
	private String awayTeam;
	private LocalDate date;
	private String time;
	private String score;
	private String replayLink; // 다시보기 링크

	// Entity → DTO 변환 메서드
	public static MatchDTO fromEntity(Match match, String replayLink) {
		if (match == null) {
			return null;
		}
		return MatchDTO.builder()
				.matchId(match.getMatchId())
				.stage(match.getStage())
				.homeTeam(match.getHomeTeam().getTeamName())
				.awayTeam(match.getAwayTeam().getTeamName())
				.date(match.getDate())
				.time(match.getTime())
				.score(match.getScore())
				.replayLink(replayLink)
				.build();
	}
}
