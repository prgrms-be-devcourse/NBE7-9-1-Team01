package com.back.api.auth.dto.request;

public record AuthRequest (
        String email,
        String password
)

{}
