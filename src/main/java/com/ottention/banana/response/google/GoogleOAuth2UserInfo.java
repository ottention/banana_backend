package com.ottention.banana.response.google;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleOAuth2UserInfo {
    private final String name;
    private final String email;
}

