package com.example.authservice.controller;

import com.example.authservice.dto.ApiResponse;
import com.example.authservice.dto.request.ReissueRequest;
import com.example.authservice.dto.request.SigninRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-service/api")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	public ResponseEntity<ApiResponse<JwtResponse>> signIn(@RequestBody SigninRequest request) {

		return ResponseEntity.ok(ApiResponse.success(authService.signIn(request)));
	}

	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<JwtResponse>> reissue(@RequestBody ReissueRequest request) {

		return ResponseEntity.ok(ApiResponse.success(authService.reissue(request)));
	}
}
