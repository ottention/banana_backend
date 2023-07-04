package com.ottention.banana.exception;

public class PrivateBusinessCardException extends Exception{

    private static final String MESSAGE = "해당 명함은 비공개 명함입니다.";

    public PrivateBusinessCardException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 403;
    }

}
