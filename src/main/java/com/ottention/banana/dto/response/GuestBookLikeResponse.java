package com.ottention.banana.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GuestBookLikeResponse {
    private final boolean isGuestBookLike;
}
