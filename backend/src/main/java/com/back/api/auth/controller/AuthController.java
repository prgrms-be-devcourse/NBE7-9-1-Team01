package com.back.api.auth.controller;

import com.back.api.auth.dto.request.AuthRequest;
import com.back.api.auth.dto.response.AuthResponse;
import com.back.api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "4. [보안] ", description = "인증 관련 API입니다.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 반환")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "JWT는 서버에서 상태를 저장하지 않으므로 클라이언트에서 삭제")
    public AuthResponse logout(@RequestBody AuthRequest authRequest) {
        return authService.logout(authRequest);
    }
}
