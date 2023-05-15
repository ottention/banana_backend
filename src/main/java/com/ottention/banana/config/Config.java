package com.ottention.banana.config;

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
public class Config implements HandlerMethodArgumentResolver {

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
        String jws = webRequest.getHeader("Authorization");
        jwtService.validateAccessToken(jws);

        String refreshToken = webRequest.getHeader("RefreshToken");
        jwtService.validateRefreshToken(refreshToken);

        try {
            Long id = jwtService.getSubject(jws);
            log.info("id = {}", id);
            return new LoginUser(id);
        } catch (JwtException e) {
            try {
                Long id = jwtService.getSubject(refreshToken);
                LoginUser loginUser = new LoginUser(id);
                return loginUser;
            } catch (JwtException ex) {
                throw new IllegalArgumentException();
            }
        }

    }
}
