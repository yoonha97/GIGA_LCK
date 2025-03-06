package com.kt.esports.dto;

import com.kt.esports.domain.Support;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportDTO {
	private Long supportId;
	private Long teamId;
	private String supportComment;
	private LocalDateTime createdAt;

	// Entity → DTO 변환 메서드
	public static SupportDTO fromEntity(Support support) {
		if (support == null) {
			return null;
		}
		return SupportDTO.builder()
				.supportId(support.getSupportId())
				.teamId(support.getTeam().getTeamId())
				.supportComment(support.getSupportComment())
				.createdAt(support.getCreatedAt())
				.build();
	}
}
