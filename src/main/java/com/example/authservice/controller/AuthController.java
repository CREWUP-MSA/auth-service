package com.example.authservice.controller;

import com.example.authservice.config.swagger.InvalidRefreshTokenApiResponse;
import com.example.authservice.config.swagger.MemberIsDeletedApiResponse;
import com.example.authservice.config.swagger.MemberNotFoundApiResponse;
import com.example.authservice.config.swagger.PasswordNotMatchedApiResponse;
import com.example.authservice.dto.CustomApiResponse;
import com.example.authservice.dto.request.ReissueRequest;
import com.example.authservice.dto.request.SigninRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-service/api")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "인증 관련 API")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	@Operation(summary = "로그인", description = "로그인을 수행합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공")
	@MemberNotFoundApiResponse
	@PasswordNotMatchedApiResponse
	public ResponseEntity<CustomApiResponse<JwtResponse>> signIn(@RequestBody SigninRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(authService.signIn(request)));
	}

	@PostMapping("/reissue")
	@Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
	@ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
	@InvalidRefreshTokenApiResponse
	@MemberIsDeletedApiResponse
	@MemberNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<JwtResponse>> reissue(@RequestBody ReissueRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(authService.reissue(request)));
	}
}
