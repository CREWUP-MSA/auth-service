package com.example.authservice.dto.client;

public record MemberResponse(
    Long id,
    String email,
    String name,
    String role,
    String password,
    String provider,
    boolean isDeleted
) {
}
