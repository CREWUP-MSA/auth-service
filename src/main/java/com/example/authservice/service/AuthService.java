package com.example.authservice.service;

import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.request.ReissueRequest;
import com.example.authservice.dto.request.SigninRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.exception.CustomException;
import com.example.authservice.exception.ErrorCode;
import com.example.authservice.service.client.MemberServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberServiceClient memberServiceClient;
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
        try {
            authenticateMember(request);

            MemberResponse response = memberServiceClient.getMemberByEmail(request.email()).data();
            validateMemberDeleted(response);

            JwtResponse jwtResponse = jwtService.createToken(response);
            redisService.saveRefreshToken(response.id().toString(), jwtResponse.refreshToken());

            return jwtResponse;

        }catch (FeignException e) {
            throw handleFeignException(e);
        }
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
        try {
            String memberId = jwtService.getSubject(request.refreshToken());

            redisService.validRefreshToken(memberId, request.refreshToken());

            MemberResponse memberResponse = memberServiceClient.getMemberById(Long.valueOf(memberId)).data();
            validateMemberDeleted(memberResponse);

            JwtResponse jwtResponse = jwtService.createToken(memberResponse);
            redisService.updateRefreshToken(memberId, jwtResponse.refreshToken());

            return jwtResponse;

        } catch (FeignException e) {
            throw handleFeignException(e);
        }
	}

    /**
     * 사용자 인증 처리 (비밀번호 확인)
     *
     * @param request 인증 요청 정보 (이메일, 비밀번호)
     * @throws CustomException 비밀번호가 일치하지 않는 경우
     */
    private void authenticateMember(SigninRequest request) {
        Boolean response = memberServiceClient.authenticateMember(request.toAuthenticateRequest()).data();

        if (!response)
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
    }

    /**
     * FeignException 처리
     *
     * @param e FeignException
     * @return CustomException (MEMBER_NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    private CustomException handleFeignException(FeignException e) {
        if (e.status() == 404)
            return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        else
            return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 회원 삭제 여부 확인
     *
     * @param memberResponse 회원 정보
     * @throws CustomException 회원이 삭제된 경우
     */
    private void validateMemberDeleted(MemberResponse memberResponse) {
        if (memberResponse.isDeleted())
            throw new CustomException(ErrorCode.MEMBER_IS_DELETED);
    }
}
