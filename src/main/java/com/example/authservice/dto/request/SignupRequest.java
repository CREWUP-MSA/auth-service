package com.example.authservice.dto.request;


public record SignupRequest (
    String name,
    String email,
    String password
) {
}
