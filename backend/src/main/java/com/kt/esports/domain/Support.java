package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Supports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Support {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long supportId;

	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	private String supportComment; // 응원댓글
	private LocalDateTime createdAt; // 생성일시
}
