package com.ottention.banana.controller;

import com.ottention.banana.dto.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.dto.response.jwt.JwtResponse;
import com.ottention.banana.dto.response.kakao.GetMemberInfoResponse;
import com.ottention.banana.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/token")
    public JwtResponse kakaoLogin(@RequestParam String accessToken) {
        GetMemberInfoResponse userInfo = socialLoginService.getKakaoMemberInfo(accessToken);
        Long userId = userService.saveFromKakao(userInfo);

        String generateAccessToken = jwtService.generateAccessToken(userId);
        String generateRefreshToken = jwtService.generateRefreshToken(userId);

        return new JwtResponse(generateAccessToken, generateRefreshToken);
    }

    @PostMapping("/idToken")
    public JwtResponse googleLogin(@RequestParam String idToken) {
        GoogleOAuth2UserInfo googleOAuth2UserInfo = socialLoginService.getGoogleMemberInfo(idToken);
        Long userId = userService.saveFromGoogle(googleOAuth2UserInfo);

        String generateAccessToken = jwtService.generateAccessToken(userId);
        String generateRefreshToken = jwtService.generateRefreshToken(userId);

        return new JwtResponse(generateAccessToken, generateRefreshToken);
    }

}
