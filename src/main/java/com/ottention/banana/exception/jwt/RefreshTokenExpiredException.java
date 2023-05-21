package com.ottention.banana.exception.jwt;

import com.ottention.banana.exception.Exception;

public class RefreshTokenExpiredException extends Exception {

    private static final String MESSAGE = "RefreshToken이 만료되었습니다.";

    public RefreshTokenExpiredException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 498;
    }
}
