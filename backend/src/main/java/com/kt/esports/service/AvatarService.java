package com.kt.esports.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AvatarService {

	private static final String DICEBEAR_API_URL = "https://api.dicebear.com/9.x/bottts/svg?seed=";

	// 랜덤 아바타 이미지 URL 생성
	public String generateRandomAvatar() {
		String randomId = UUID.randomUUID().toString(); // UUID를 이용한 랜덤 값 생성
		return DICEBEAR_API_URL + randomId; // DiceBear API URL 조합
	}
}
