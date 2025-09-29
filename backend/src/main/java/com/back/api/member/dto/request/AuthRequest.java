package com.back.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AuthRequest", description = "로그인 요청 DTO")
public record AuthRequest(
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        @NotNull(message = "이메일은 필수 입니다.")
        String email,
        @Schema(description = "비밀번호", example = "1234")
        @NotNull(message = "비밀번호는 필수 입니다.")
        String password
) {}
