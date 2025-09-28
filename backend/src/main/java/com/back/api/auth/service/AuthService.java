package com.back.api.auth.service;

import com.back.api.auth.dto.request.AuthRequest;
import com.back.api.auth.dto.response.AuthResponse;
import com.back.domain.auth.entity.Auth;
import com.back.domain.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    @Transactional(readOnly = true)
    public AuthResponse login (AuthRequest authRequest){
        Auth auth = authRepository.findByEmail(authRequest.email());
        if(auth == null){
            return new AuthResponse(false, "이메일이 일치하지 않습니다.");
        }
        if(!auth.getPassword().equals(authRequest.password())){
            return new AuthResponse(false, "비밀번호 틀림.");
        }
        return new AuthResponse(true, "로그인 성공");
    }

    @Transactional(readOnly = true)
    public AuthResponse logout (AuthRequest authRequest){
        return new AuthResponse(true, "로그아웃 성공");
    }

    public Auth findByEmail (String email){
        return authRepository.findByEmail(email);
    }

}
