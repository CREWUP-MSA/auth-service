package com.example.authservice.dto.response;

import lombok.Builder;

@Builder
public record JwtResponse(
    String accessToken,
    String refreshToken,
    String tokenType
) {
}
