package com.back.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 DTO")
public record AuthRequest(
        @Schema(description = "사용자 이메일", example = "user1@naver.com") String email,
        @Schema(description = "비밀번호", example = "1234") String password
) {}
