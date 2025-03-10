package com.kt.esports.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long accountId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(name = "riot_id", nullable = false, unique = true)
	private String riotId;

	@Column(name = "tag_line", nullable = false)
	private String tagLine;
}