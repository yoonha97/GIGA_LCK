package com.kt.esports.controller;

import com.kt.esports.dto.PlayerDTO;
import com.kt.esports.dto.TeamDTO;
import com.kt.esports.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

	private final TeamService teamService;

	@Autowired
	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	// 모든 팀 조회
	@GetMapping
	public ResponseEntity<List<TeamDTO>> getAllTeams() {
		return ResponseEntity.ok(teamService.getAllTeams());
	}

	// ID로 팀 조회
	@GetMapping("/{teamId}")
	public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long teamId) {
		return ResponseEntity.ok(teamService.getTeamById(teamId));
	}

	// 팀 이름으로 팀 조회
	@GetMapping("/by-name/{teamName}")
	public ResponseEntity<TeamDTO> getTeamByName(@PathVariable String teamName) {
		return ResponseEntity.ok(teamService.getTeamByName(teamName));
	}

	// 팀 소속 선수 목록 조회
	@GetMapping("/{teamId}/players")
	public ResponseEntity<List<PlayerDTO>> getTeamPlayers(@PathVariable Long teamId) {
		List<PlayerDTO> players = teamService.getTeamPlayers(teamId);
		return ResponseEntity.ok(players);
	}
}