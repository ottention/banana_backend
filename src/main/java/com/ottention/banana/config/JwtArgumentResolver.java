package com.ottention.banana.config;

import com.ottention.banana.exception.jwt.Unauthorized;
import com.ottention.banana.request.LoginUser;
import com.ottention.banana.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasLoginType = LoginUser.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasLoginType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        log.info("resolveArgument 실행");
        String accessToken = webRequest.getHeader("Authorization");
        jwtService.validateAccessToken(accessToken);

        try {
            Long userId = jwtService.getSubject(accessToken);
            log.info("userId = {}", userId);
            return new LoginUser(userId);
        } catch (JwtException e) {
            throw new Unauthorized();
        }

    }
}
