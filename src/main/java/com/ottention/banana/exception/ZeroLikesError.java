package com.ottention.banana.exception;

public class ZeroLikesError extends Exception {

    private static final String MESSAGE = "좋아요가 0인 명함은 좋아요 취소할 수 없습니다.";

    public ZeroLikesError() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 400;
    }

}
