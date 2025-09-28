package com.back.api.auth.controller;

import com.back.api.auth.dto.request.AuthRequest;
import com.back.api.auth.dto.response.AuthResponse;
import com.back.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login (@RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }

    @PostMapping("/logout")
    public AuthResponse logout (@RequestBody AuthRequest authRequest){
        return authService.logout(authRequest);
    }
}
