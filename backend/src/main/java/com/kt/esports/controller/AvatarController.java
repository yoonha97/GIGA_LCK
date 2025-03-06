package com.kt.esports.controller;

import com.kt.esports.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {

	private final AvatarService avatarService;

	// 랜덤 아바타 URL을 반환하는 API
	@GetMapping("/random")
	public ResponseEntity<String> getRandomAvatar() {
		String avatarUrl = avatarService.generateRandomAvatar();
		return ResponseEntity.ok(avatarUrl);
	}
}
