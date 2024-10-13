package com.example.authservice.config.oauth2;

import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo (
        String name,
        String email,
        String nickname
){

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "naver" -> ofNaver(attributes);
            case "kakao" -> ofKakao(attributes);
            case "github" -> ofGithub(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name(attributes.get("name").toString())
                .nickname(attributes.get("email").toString())
                .email(attributes.get("email").toString())
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .name(response.get("name").toString())
                .email(response.get("email").toString())
                .nickname(response.get("nickname").toString())
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2UserInfo.builder()
                .name(kakaoAccount.get("name").toString())
                .email(kakaoAccount.get("email").toString())
                .nickname(profile.get("nickname").toString())
                .build();
    }

    private static OAuth2UserInfo ofGithub(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .nickname(attributes.get("login").toString())
                .build();
    }

    public Member toEntity(String provider) {
        return Member.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .provider(provider)
                .build();
    }
}
