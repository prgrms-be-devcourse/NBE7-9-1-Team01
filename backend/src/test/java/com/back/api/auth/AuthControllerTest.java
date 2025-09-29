package com.back.api.auth;

import com.back.api.auth.dto.request.AuthRequest;
import com.back.api.auth.dto.response.AuthResponse;
import com.back.api.auth.service.AuthService;
import com.back.domain.auth.entity.Auth;
import com.back.domain.auth.repository.AuthRepository;
import com.back.global.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthRepository authRepository;
    private JwtTokenProvider jwtTokenProvider;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authRepository = mock(AuthRepository.class);
        jwtTokenProvider = new JwtTokenProvider();
        authService = new AuthService(authRepository, jwtTokenProvider);
    }

    @Test
    void login_success_shouldReturnToken() {
        // given
        String email = "user1@naver.com";
        String password = "1234";
        Auth auth = new Auth(email, password); // 엔티티 생성
        when(authRepository.findByEmail(email)).thenReturn(auth);

        AuthRequest request = new AuthRequest(email, password);

        // when
        AuthResponse response = authService.login(request);

        // then
        assertTrue(response.success());
        assertNotNull(response.token());
        assertEquals("로그인 성공", response.message());
    }

    @Test
    void login_fail_wrongPassword() {
        // given
        String email = "user1@naver.com";
        Auth auth = new Auth(email, "1111");
        when(authRepository.findByEmail(email)).thenReturn(auth);

        AuthRequest request = new AuthRequest(email, "2222");

        // when
        AuthResponse response = authService.login(request);

        // then
        assertFalse(response.success());
        assertEquals("비밀번호 틀림.", response.message());
        assertNull(response.token());
    }

    @Test
    void login_fail_emailNotFound() {
        // given
        String email = "user111@naver.com";
        when(authRepository.findByEmail(email)).thenReturn(null);

        AuthRequest request = new AuthRequest(email, "1111");

        // when
        AuthResponse response = authService.login(request);

        // then
        assertFalse(response.success());
        assertEquals("이메일이 일치하지 않습니다.", response.message());
        assertNull(response.token());
    }

    @Test
    void logout_shouldReturnSuccess() {
        // given
        AuthRequest request = new AuthRequest("user1@naver.com", "1111");

        // when
        AuthResponse response = authService.logout(request);

        // then
        assertTrue(response.success());
        assertEquals("로그아웃 성공", response.message());
        assertNull(response.token());
    }
}
