package com.kt.esports.dto;

import com.kt.esports.domain.Player;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDTO {
	private Long playerId;
	private Long teamId;
	private String playerName;
	private String position;
	private String description;

	// Entity → DTO 변환 메서드
	public static PlayerDTO fromEntity(Player player) {
		if (player == null) {
			return null;
		}
		return PlayerDTO.builder()
				.playerId(player.getPlayerId())
				.teamId(player.getTeam() != null ? player.getTeam().getTeamId() : null) // FA 선수 처리
				.playerName(player.getPlayerName())
				.position(player.getPosition())
				.description(player.getDescription())
				.build();
	}
}
