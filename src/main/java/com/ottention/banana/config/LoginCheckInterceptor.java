package com.ottention.banana.config;

import com.ottention.banana.exception.jwt.Unauthorized;
import com.ottention.banana.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 체크 인터셉터
 * Authorization헤더에 있는 accessToken 값 검증 후 통과면 true 반환 아니면 예외
 */
@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("Authorization");
        jwtService.validateAccessToken(accessToken);

        try {
            jwtService.getSubject(accessToken);
        } catch (JwtException e) {
            log.info("미인증 사용자 요청");
            throw new Unauthorized();
        }

        return true;
    }
}
