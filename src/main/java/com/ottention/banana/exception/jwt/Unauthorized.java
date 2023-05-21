package com.ottention.banana.exception.jwt;

import com.ottention.banana.exception.Exception;

public class Unauthorized extends Exception {

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 401;
    }

}
