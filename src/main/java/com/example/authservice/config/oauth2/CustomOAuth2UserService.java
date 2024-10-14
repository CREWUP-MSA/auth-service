package com.example.authservice.config.oauth2;

import com.example.authservice.dto.client.ClientResponse;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.exception.CustomException;
import com.example.authservice.exception.ErrorCode;
import com.example.authservice.service.JwtService;
import com.example.authservice.service.client.MemberServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberServiceClient memberServiceClient;
    private final JwtService jwtService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);

        MemberResponse memberResponse = getOrSave(oAuth2UserInfo, registrationId);

        JwtResponse jwtResponse = jwtService.createToken(memberResponse);

        return new CustomOAuth2User(memberResponse, attributes, jwtResponse);
    }

    private MemberResponse getOrSave(OAuth2UserInfo oAuth2UserInfo, String provider) {
        try{
            /**
             * 회원 조회후 존재할시 회원 정보 반환
             */
            ClientResponse<MemberResponse> response = memberServiceClient.getMemberByEmail(oAuth2UserInfo.email());

            return response.data();
        }catch (FeignException e) {
            /**
             * 404: 회원이 존재하지 않을 경우 회원 생성
             */
            if (e.status() == 404)
                return memberServiceClient.createMember(oAuth2UserInfo.toEntity(provider)).data();

            /**
             * 그 외의 경우 서버 에러
             */
            else
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
