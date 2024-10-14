package com.example.authservice.config.oauth2;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.authservice.dto.client.MemberResponse;
import com.example.authservice.dto.response.JwtResponse;

public record CustomOAuth2User (
	MemberResponse memberResponse,
	Map<String, Object> attributes,
	JwtResponse jwtResponse

) implements OAuth2User {

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(memberResponse.role()));
	}

	@Override
	public String getName() {
		return memberResponse.id().toString();
	}
}
