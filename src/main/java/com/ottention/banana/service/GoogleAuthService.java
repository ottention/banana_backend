package com.ottention.banana.service;

import com.ottention.banana.api.GoogleAuthApi;
import com.ottention.banana.response.google.GoogleOAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleAuthApi googleAuthApi;

    public GoogleOAuth2UserInfo googleLogin(String idToken)  {
        return googleAuthApi.getMemberInfo(idToken);
    }

}
