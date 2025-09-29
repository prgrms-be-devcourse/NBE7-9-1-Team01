package com.back.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenProvider {

    private String secretKey = "asbcdefghijklmnopqrstuvwxyz1234567890"; // JWT 서명 키
    private long expireSeconds = 1000L * 60 * 60; // 1시간

    // JWT 발급
    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireSeconds))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // JWT에서 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
