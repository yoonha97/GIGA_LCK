package com.kt.esports.dto;

import com.kt.esports.domain.Team;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDTO {
	private Long teamId;
	private String teamName;
	private String owner;
	private String headCoach;
	private List<String> coaches;
	private List<String> players;

	// Entity → DTO 변환 메서드
	public static TeamDTO fromEntity(Team team) {
		if (team == null) {
			return null;
		}
		return TeamDTO.builder()
				.teamId(team.getTeamId())
				.teamName(team.getTeamName())
				.owner(team.getOwner())
				.headCoach(team.getHeadCoach())
				.coaches(team.getCoaches().stream()
						.map(coach -> coach.getCoachName())
						.collect(Collectors.toList()))
				.players(team.getPlayers().stream()
						.map(player -> player.getPlayerName())
						.collect(Collectors.toList()))
				.build();
	}
}
