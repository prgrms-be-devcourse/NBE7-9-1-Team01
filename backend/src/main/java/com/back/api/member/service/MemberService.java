package com.back.api.member.service;

import com.back.api.member.dto.request.AuthRequest;
import com.back.api.member.dto.response.AuthResponse;
import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import com.back.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 발급 서비스

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        Member member = memberRepository.findByEmail(authRequest.email());
        if (member == null) {
            return new AuthResponse(false, "이메일이 일치하지 않습니다.");
        }
        if (!member.getPassword().equals(authRequest.password())) {
            return new AuthResponse(false, "비밀번호 틀림.");
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole());

        return new AuthResponse(true, "로그인 성공", token);
    }

    @Transactional(readOnly = true)
    public AuthResponse logout(AuthRequest authRequest) {
        // JWT는 서버에서 상태를 저장하지 않으므로, 프론트가 토큰을 버리면 로그아웃 처리됨
        return new AuthResponse(true, "로그아웃 성공");
    }

    @Transactional
    public Member create(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return memberRepository.count();
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
