package com.example.authservice.dto.request;

import lombok.Builder;

@Builder
public record MemberRequest(
	String email,
	String name,
	String provider
) {
}
