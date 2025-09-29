package com.back.api.auth.dto.response;

public record AuthResponse(
    boolean success,
    String message,
    String token
        ){
    public AuthResponse(boolean success, String message) {
        this(success, message, null);
    }

}
