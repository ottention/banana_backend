package com.ottention.banana.dto.response.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReissueResponse {
    private final String accessToken;
}
