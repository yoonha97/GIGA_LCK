package com.kt.esports.dto;

import com.kt.esports.domain.Coach;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDTO {
	private Long coachId;
	private Long teamId;
	private String coachName;

	// Entity → DTO 변환 메서드
	public static CoachDTO fromEntity(Coach coach) {
		if (coach == null) {
			return null;
		}
		return CoachDTO.builder()
				.coachId(coach.getCoachId())
				.teamId(coach.getTeam().getTeamId())
				.coachName(coach.getCoachName())
				.build();
	}
}
