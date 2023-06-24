package com.ottention.banana.dto.response.businesscard;

import com.ottention.banana.entity.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImageResponse {
    private final String imageUrl;
    private final Coordinate coordinate;
}
