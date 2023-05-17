package com.ottention.banana.controller;

import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.response.JwtResponse;
import com.ottention.banana.response.kakao.GetMemberInfoResponse;
import com.ottention.banana.service.GoogleAuthService;
import com.ottention.banana.service.JwtService;
import com.ottention.banana.service.KakaoAuthService;
import com.ottention.banana.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {

    private final KakaoAuthService kakaoAuthService;
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/token")
    public JwtResponse token(@RequestParam String accessToken) {
        GetMemberInfoResponse userInfo = kakaoAuthService.getMemberInfo(accessToken);
        Long userId = userService.saveFromKakao(userInfo);

        String generateAccessToken = jwtService.generateAccessToken(userId);
        String generateRefreshToken = jwtService.generateRefreshToken(userId);

        return new JwtResponse(generateAccessToken, generateRefreshToken);
    }

    @PostMapping("/idToken")
    public JwtResponse googleLogin(@RequestParam String idToken) {
        GoogleOAuth2UserInfo googleOAuth2UserInfo = googleAuthService.googleLogin(idToken);
        Long userId = userService.saveFromGoogle(googleOAuth2UserInfo);

        String generateAccessToken = jwtService.generateAccessToken(userId);
        String generateRefreshToken = jwtService.generateRefreshToken(userId);

        return new JwtResponse(generateAccessToken, generateRefreshToken);
    }

}
