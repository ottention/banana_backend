package com.ottention.banana.service;

import com.ottention.banana.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60L * 60L * 24L * 7L; // 액세스 토큰의 만료 시간: 7일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60L * 60L * 24L * 30L; // 리프레시 토큰의 만료 시간: 30일

    public String generateAccessToken(Long userId) {
        SecretKey key = getSecretKey();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        SecretKey key = getSecretKey();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .compact();
    }

    public Long getSubject(String jws) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getJwtKey())
                .build()
                .parseClaimsJws(jws);
        return Long.parseLong(claims.getBody().getSubject());
    }

    public void validateAccessToken(String jws) {
        if (jws == null || jws.equals("")) {
            throw new IllegalArgumentException("AccessToken이 없습니다.");
        }
    }

    public void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.equals("")) {
            throw new RuntimeException("RefreshToken이 없습니다.");
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getJwtKey());
    }
}
