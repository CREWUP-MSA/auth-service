package com.example.authservice.dto.request;

public record SigninRequest(
    String email,
    String password
) {
}
