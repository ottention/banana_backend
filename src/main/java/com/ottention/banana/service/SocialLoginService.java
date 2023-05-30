package com.ottention.banana.service;

import com.ottention.banana.api.GoogleAuthApi;
import com.ottention.banana.api.KakaoAuthApi;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import com.ottention.banana.response.kakao.GetMemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final KakaoAuthApi kakaoAuthApi;
    private final GoogleAuthApi googleAuthApi;

    public GetMemberInfoResponse getKakaoMemberInfo(String accessToken){
        return kakaoAuthApi.getMemberInfo(accessToken);
    }

    public GoogleOAuth2UserInfo getGoogleMemberInfo(String idToken)  {
        return googleAuthApi.getMemberInfo(idToken);
    }

}
