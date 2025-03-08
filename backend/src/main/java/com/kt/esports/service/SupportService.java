package com.kt.esports.service;

import com.kt.esports.domain.Support;
import com.kt.esports.domain.Team;
import com.kt.esports.dto.SupportDTO;
import com.kt.esports.repository.SupportRepository;
import com.kt.esports.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {
	private final SupportRepository supportRepository;
	private final TeamRepository teamRepository;

	// 팀별 응원 댓글 목록 조회
	@Transactional(readOnly = true)
	public List<SupportDTO> getSupportsByTeamId(Long teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));

		List<Support> supports = supportRepository.findByTeam(team);
		return supports.stream()
				.map(SupportDTO::fromEntity)
				.collect(Collectors.toList());
	}

	// 응원 댓글 생성
	@Transactional
	public SupportDTO createSupport(SupportDTO supportDTO) {
		Team team = teamRepository.findById(supportDTO.getTeamId())
				.orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + supportDTO.getTeamId()));

		Support support = Support.builder()
				.team(team)
				.supportComment(supportDTO.getSupportComment())
				.createdAt(LocalDateTime.now())
				.build();

		Support savedSupport = supportRepository.save(support);
		return SupportDTO.fromEntity(savedSupport);
	}

	// 응원 댓글 상세 조회
	@Transactional(readOnly = true)
	public SupportDTO getSupportById(Long supportId) {
		Support support = supportRepository.findById(supportId)
				.orElseThrow(() -> new IllegalArgumentException("Support comment not found with id: " + supportId));

		return SupportDTO.fromEntity(support);
	}

	// 응원 댓글 수정
	@Transactional
	public SupportDTO updateSupport(Long supportId, SupportDTO supportDTO) {
		Support support = supportRepository.findById(supportId)
				.orElseThrow(() -> new IllegalArgumentException("Support comment not found with id: " + supportId));

		// 응원 댓글 내용만 수정 가능 (팀 변경 불가)
		support.setSupportComment(supportDTO.getSupportComment());

		Support updatedSupport = supportRepository.save(support);
		return SupportDTO.fromEntity(updatedSupport);
	}

	// 응원 댓글 삭제
	@Transactional
	public void deleteSupport(Long supportId) {
		if (!supportRepository.existsById(supportId)) {
			throw new IllegalArgumentException("Support comment not found with id: " + supportId);
		}
		supportRepository.deleteById(supportId);
	}
}