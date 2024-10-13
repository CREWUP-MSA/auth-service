package com.example.authservice.config.oauth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.authservice.dto.ApiResponse;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RedisService redisService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		MemberResponse memberResponse = oAuth2User.memberResponse();
		JwtResponse jwtResponse = oAuth2User.jwtResponse();

		// Redis에 refresh token 저장
		redisService.saveRefreshToken(memberResponse.id().toString(), jwtResponse.refreshToken());

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		ApiResponse<JwtResponse> apiResponse = ApiResponse.success(jwtResponse);
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
	}
}
