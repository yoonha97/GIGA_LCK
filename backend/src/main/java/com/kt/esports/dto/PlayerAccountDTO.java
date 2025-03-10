package com.kt.esports.dto;

import com.kt.esports.domain.PlayerAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAccountDTO {
	private Long accountId;
	private Long playerId;
	private String playerName; // 추가 정보: 선수 이름
	private String riotId;
	private String tagLine; // 추가된 필드

	// Entity → DTO 변환 메서드
	public static PlayerAccountDTO fromEntity(PlayerAccount playerAccount) {
		if (playerAccount == null) {
			return null;
		}

		return PlayerAccountDTO.builder()
				.accountId(playerAccount.getAccountId())
				.playerId(playerAccount.getPlayer().getPlayerId())
				.playerName(playerAccount.getPlayer().getPlayerName())
				.riotId(playerAccount.getRiotId())
				.tagLine(playerAccount.getTagLine())
				.build();
	}
}