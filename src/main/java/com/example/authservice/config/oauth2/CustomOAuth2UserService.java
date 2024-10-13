package com.example.authservice.config.oauth2;

import com.example.authservice.dto.client.ClientResponse;
import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.service.JwtService;
import com.example.authservice.service.client.MemberServiceClient;
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
        ClientResponse<MemberResponse> response = memberServiceClient.getMemberByEmail(oAuth2UserInfo.email());

        if (response.data() == null)
            return memberServiceClient.saveMember(oAuth2UserInfo.toEntity(provider)).data();

        return response.data();
    }
}
