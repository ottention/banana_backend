package com.ottention.banana.exception.jwt;

import com.ottention.banana.exception.Exception;

public class RefreshTokenNotFoundException extends Exception {

    private static final String MESSAGE = "RefreshToken이 없습니다.";

    public RefreshTokenNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }

}
