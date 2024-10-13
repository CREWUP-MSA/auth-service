package com.example.authservice.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	@Value("${jwt.refresh.expiration}")
	private long refreshTokenExpiration;

	private final RedisTemplate<String, Object> redisTemplate;

	public void saveRefreshToken(String memberId, String refreshToken) {
		redisTemplate.opsForValue().set(memberId, refreshToken, refreshTokenExpiration, TimeUnit.MICROSECONDS);
	}

	public String getRefreshToken(String memberId) {
		return (String) redisTemplate.opsForValue().get(memberId);
	}

}
