package com.back.api.member.controller;

import com.back.api.member.dto.request.AuthRequest;
import com.back.api.member.dto.response.AuthResponse;

import com.back.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "5. [관리자 로그인] ", description = "인증 관련 API입니다.")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인 후 JWT 반환")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return memberService.login(authRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "JWT는 서버에서 상태를 저장하지 않으므로 클라이언트에서 삭제")
    public AuthResponse logout(@RequestBody AuthRequest authRequest) {
        return memberService.logout(authRequest);
    }
}
