package com.ottention.banana.exception;

public class BusinessCardLimitExceededException extends Exception {

    private static final String MESSAGE = "각 사용자는 최대 3개의 명함만 생성할 수 있습니다.";

    public BusinessCardLimitExceededException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 409;
    }

}
