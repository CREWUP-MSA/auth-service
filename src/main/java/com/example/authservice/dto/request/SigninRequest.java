package com.example.authservice.dto.request;

import com.example.authservice.dto.client.AuthenticateRequest;

public record SigninRequest(
    String email,
    String password
) {

    public AuthenticateRequest toAuthenticateRequest() {
        return new AuthenticateRequest(email(), password());
    }
}
