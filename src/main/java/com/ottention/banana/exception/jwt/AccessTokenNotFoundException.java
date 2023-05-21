package com.ottention.banana.exception.jwt;

import com.ottention.banana.exception.Exception;

public class AccessTokenNotFoundException extends Exception {

    private static final String MESSAGE = "AccessToken이 없습니다.";

    public AccessTokenNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
