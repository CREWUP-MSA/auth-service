package com.example.authservice.dto.client;

public record ClientResponse<T> (
    T data,
    String message
) {
}
