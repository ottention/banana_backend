package com.ottention.banana.response.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReissueResponse {
    private final String accessToken;
}
