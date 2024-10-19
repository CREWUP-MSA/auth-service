package com.example.authservice.service;

import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.client.mapper.MemberClientMapper;
import com.example.authservice.dto.request.ReissueRequest;
import com.example.authservice.dto.request.SigninRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberClientMapper memberClientMapper;
    private final JwtService jwtService;
    private final RedisService redisService;

    /**
     * 사용자 로그인 처리
     *
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return JwtResponse (accessToken, refreshToken, tokenType)
     * @throws CustomException 회원을 찾을 수 없는 경우, 비밀번호가 일치하지 않는 경우
     */
    @Transactional
    public JwtResponse signIn(SigninRequest request) {
        memberClientMapper.authenticateMember(request.toAuthenticateRequest());

        MemberResponse response = memberClientMapper.getMemberByEmail(request.email());

        JwtResponse jwtResponse = jwtService.createToken(response);
        redisService.saveRefreshToken(response.id().toString(), jwtResponse.refreshToken());

        return jwtResponse;
    }

    /**
     * Refresh Token 을 이용한 토큰 재발급
     *
     * @param request 재발급 요청 정보 (refreshToken)
     * @return JwtResponse (accessToken, refreshToken, tokenType)
     * @throws CustomException 회원을 찾을 수 없는 경우, 삭제된 회원인 경우, refreshToken 이 유효하지 않은 경우
     */
    @Transactional
	public JwtResponse reissue(ReissueRequest request) {
        String memberId = jwtService.getSubject(request.refreshToken());

        redisService.validRefreshToken(memberId, request.refreshToken());

        MemberResponse memberResponse = memberClientMapper.getMemberById(Long.parseLong(memberId));

        JwtResponse jwtResponse = jwtService.createToken(memberResponse);
        redisService.updateRefreshToken(memberId, jwtResponse.refreshToken());

        return jwtResponse;
	}
}
