package com.ottention.banana.dto.response.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
}
