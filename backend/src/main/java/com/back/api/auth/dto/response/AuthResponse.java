package com.back.api.auth.dto.response;

public record AuthResponse(
    boolean success,
    String message
        ){}
