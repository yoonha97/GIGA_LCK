package com.kt.esports.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "Teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long teamId;

	@Column(nullable = false)
	private String teamName; // 팀 이름

	private String owner; // 단장
	private String headCoach; // 감독

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Coach> coaches; // 코치 목록

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Player> players; // 선수 목록
}
