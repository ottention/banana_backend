package com.ottention.banana.exception;

public class DuplicationLikeException extends Exception {

    private static final String MESSAGE = "이미 좋아요를 누른 명함입니다.";

    public DuplicationLikeException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 409;
    }

}
