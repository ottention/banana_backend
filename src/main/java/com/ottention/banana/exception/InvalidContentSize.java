package com.ottention.banana.exception;

public class InvalidContentSize extends Exception {

    private static final String MESSAGE = "유효하지 않는 사이즈입니다.";

    public InvalidContentSize() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 400;
    }

}
