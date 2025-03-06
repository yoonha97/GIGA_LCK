package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Coaches")
public class Coach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coachId;

	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team team; // 팀 ID 참조 (FK)

	@Column(nullable = false)
	private String coachName; // 코치 이름
}
