package com.ottention.banana.exception;

public class BusinessCardNotFound extends Exception {

    private static final String MESSAGE = "존재하지 않는 명함입니다.";

    public BusinessCardNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }

}
