package com.ottention.banana.exception;

public class TagLimitExceededException extends Exception {

    private static final String MESSAGE = "태그는 최대 10개까지 작성 가능합니다.";

    public TagLimitExceededException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 422;
    }

}
