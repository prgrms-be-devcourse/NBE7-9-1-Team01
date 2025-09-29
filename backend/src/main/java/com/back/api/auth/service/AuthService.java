package com.back.api.auth.service;

import com.back.api.auth.dto.request.AuthRequest;
import com.back.api.auth.dto.response.AuthResponse;
import com.back.domain.auth.entity.Auth;
import com.back.domain.auth.repository.AuthRepository;
import com.back.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 발급 서비스

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        Auth auth = authRepository.findByEmail(authRequest.email());
        if (auth == null) {
            return new AuthResponse(false, "이메일이 일치하지 않습니다.");
        }
        if (!auth.getPassword().equals(authRequest.password())) {
            return new AuthResponse(false, "비밀번호 틀림.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(auth.getEmail());

        return new AuthResponse(true, "로그인 성공", token);
    }

    @Transactional(readOnly = true)
    public AuthResponse logout(AuthRequest authRequest) {
        // JWT는 서버에서 상태를 저장하지 않으므로, 프론트가 토큰을 버리면 로그아웃 처리됨
        return new AuthResponse(true, "로그아웃 성공");
    }

    public Auth findByEmail(String email) {
        return authRepository.findByEmail(email);
    }
}
