package com.kt.esports.controller;

import com.kt.esports.dto.SupportDTO;
import com.kt.esports.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams/{teamId}/supports")
@RequiredArgsConstructor
public class SupportController {
	private final SupportService supportService;

	// 팀별 응원 댓글 목록 조회
	@GetMapping
	public ResponseEntity<List<SupportDTO>> getSupportsByTeamId(@PathVariable Long teamId) {
		List<SupportDTO> supports = supportService.getSupportsByTeamId(teamId);
		return ResponseEntity.ok(supports);
	}

	// 응원 댓글 생성
	@PostMapping
	public ResponseEntity<SupportDTO> createSupport(
			@PathVariable Long teamId,
			@RequestBody SupportDTO supportDTO) {
		supportDTO.setTeamId(teamId); // URL의 팀 ID를 DTO에 설정
		SupportDTO createdSupport = supportService.createSupport(supportDTO);
		return ResponseEntity.ok(createdSupport);
	}

	// 응원 댓글 상세 조회
	@GetMapping("/{supportId}")
	public ResponseEntity<SupportDTO> getSupportById(
			@PathVariable Long teamId,
			@PathVariable Long supportId) {
		SupportDTO support = supportService.getSupportById(supportId);
		// 요청한 팀 ID와 실제 댓글의 팀 ID가 일치하는지 검증
		if (!support.getTeamId().equals(teamId)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(support);
	}

	// 응원 댓글 수정
	@PutMapping("/{supportId}")
	public ResponseEntity<SupportDTO> updateSupport(
			@PathVariable Long teamId,
			@PathVariable Long supportId,
			@RequestBody SupportDTO supportDTO) {
		// 댓글 검증
		SupportDTO existingSupport = supportService.getSupportById(supportId);
		// 요청한 팀 ID와 실제 댓글의 팀 ID가 일치하는지 검증
		if (!existingSupport.getTeamId().equals(teamId)) {
			return ResponseEntity.badRequest().build();
		}

		// 팀 ID 변경 없이 댓글 내용만 업데이트
		SupportDTO updatedSupport = supportService.updateSupport(supportId, supportDTO);
		return ResponseEntity.ok(updatedSupport);
	}

	// 응원 댓글 삭제
	@DeleteMapping("/{supportId}")
	public ResponseEntity<Void> deleteSupport(
			@PathVariable Long teamId,
			@PathVariable Long supportId) {
		// 해당 supportId가 팀에 속하는지 검증하는 로직은 생략 (필요시 추가)
		supportService.deleteSupport(supportId);
		return ResponseEntity.noContent().build();
	}
}