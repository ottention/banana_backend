package com.ottention.banana.service;

import com.ottention.banana.config.JwtConfig;
import com.ottention.banana.exception.jwt.AccessTokenNotFoundException;
import com.ottention.banana.exception.jwt.RefreshTokenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JwtServiceTest {

    private JwtService jwtService;
    private JwtConfig jwtConfig;

    @Value("${jwt.jwt-key}")
    private String jwtKey;

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        jwtConfig.setJwtKey(jwtKey);
        jwtService = new JwtService(jwtConfig);
    }

    @Test
    @DisplayName("AccessToken 생성 테스트")
    void generateAccessToken_shouldGenerateValidAccessToken() {
        //given
        Long userId = 123L;

        //when
        String accessToken = jwtService.generateAccessToken(userId);

        //then
        assertNotNull(accessToken);
    }

    @Test
    @DisplayName("refreshToken 생성 테스트")
    void generateRefreshToken_shouldGenerateValidRefreshToken() {
        //given
        Long userId = 123L;

        //when
        String refreshToken = jwtService.generateRefreshToken(userId);

        //then
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("AccessToken 예외(null) 테스트")
    void validateAccessToken_shouldThrowExceptionForInvalidToken() {
        //given
        String accessToken = null;

        //then
        assertThrows(AccessTokenNotFoundException.class, () -> jwtService.validateAccessToken(accessToken));
    }

    @Test
    @DisplayName("RefreshToken 예외(null) 테스트")
    void validateRefreshToken_shouldThrowExceptionForInvalidToken() {
        //given
        String refreshToken = null;

        //then
        assertThrows(RefreshTokenNotFoundException.class, () -> jwtService.reissueAccessToken(refreshToken));
    }

}