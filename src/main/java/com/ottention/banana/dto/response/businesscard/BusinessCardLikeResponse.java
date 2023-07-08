package com.ottention.banana.dto.response.businesscard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessCardLikeResponse {
    private final int likeCount;
    private final boolean isLike;
}
