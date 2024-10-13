package com.example.authservice.service;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.response.JwtResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private long accessTokenExpiration;

	@Value("${jwt.refresh.expiration}")
	private long refreshTokenExpiration;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public JwtResponse createToken(MemberResponse response) {
		Claims claims = Jwts.claims().setSubject(response.id().toString());
		claims.put("role", response.role());

		Date now = new Date();
		Date accessTokenExpiration = new Date(now.getTime() + this.accessTokenExpiration);
		Date refreshTokenExpiration = new Date(now.getTime() + this.refreshTokenExpiration);

		String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(accessTokenExpiration)
			.signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();

		String refreshToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(refreshTokenExpiration)
			.signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();

		return JwtResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.tokenType("Bearer")
			.build();
	}

}
