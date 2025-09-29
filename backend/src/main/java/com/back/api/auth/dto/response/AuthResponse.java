package com.back.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인/로그아웃 응답 DTO")
public record AuthResponse(
        @Schema(description = "성공 여부") boolean success,
        @Schema(description = "응답 메시지") String message,
        @Schema(description = "JWT 토큰", nullable = true) String token
) {
    public AuthResponse(boolean success, String message) {
        this(success, message, null);
    }
}
