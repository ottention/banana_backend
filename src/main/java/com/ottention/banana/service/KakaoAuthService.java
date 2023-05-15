package com.ottention.banana.service;

import com.ottention.banana.api.KakaoAuthApi;
import com.ottention.banana.response.kakao.GetMemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAuthApi kakaoAuthApi;

    public GetMemberInfoResponse getMemberInfo(String accessToken){
        GetMemberInfoResponse memberInfo = kakaoAuthApi.getMemberInfo(accessToken);
        return memberInfo;
    }
}
