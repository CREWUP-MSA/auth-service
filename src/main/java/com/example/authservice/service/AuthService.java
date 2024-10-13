package com.example.authservice.service;

import com.example.authservice.dto.client.ClientResponse;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.request.SigninRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.exception.CustomException;
import com.example.authservice.exception.ErrorCode;
import com.example.authservice.service.client.MemberServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberServiceClient memberServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisService redisService;

    @Transactional
    public JwtResponse signIn(SigninRequest request) {
        try {
            ClientResponse<MemberResponse> response = memberServiceClient.getMemberByEmail(request.email());

            if (response.data().isDeleted())
                throw new CustomException(ErrorCode.MEMBER_IS_DELETED);

            if (!passwordEncoder.matches(request.password(), response.data().password()))
                throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);

            JwtResponse jwtResponse = jwtService.createToken(response.data());
            redisService.saveRefreshToken(response.data().id().toString(), jwtResponse.refreshToken());

            return jwtResponse;

        }catch (FeignException e) {
            if (e.status() == 404)
                throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            else
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
