package com.example.authservice.dto.client;

public record AuthenticateRequest(
    String email,
    String password
) {
}
