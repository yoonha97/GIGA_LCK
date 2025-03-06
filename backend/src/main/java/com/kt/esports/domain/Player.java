package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long playerId;

	@ManyToOne
	@JoinColumn(name = "team_id", nullable = true)
	private Team team;

	private String playerName; // 선수 이름
	private String position; // 포지션
	private String description; // 설명
}
