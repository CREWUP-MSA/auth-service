package com.example.authservice.service;

import java.util.concurrent.TimeUnit;

import com.example.authservice.exception.CustomException;
import com.example.authservice.exception.ErrorCode;
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

	public void validRefreshToken(String memberId, String refreshToken) {
		String savedRefreshToken = (String) redisTemplate.opsForValue().get(memberId);

		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken))
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

	}


	public void updateRefreshToken(String memberId, String refreshToken) {
		redisTemplate.delete(memberId);
		redisTemplate.opsForValue().set(memberId, refreshToken, refreshTokenExpiration, TimeUnit.MICROSECONDS);
	}

}
