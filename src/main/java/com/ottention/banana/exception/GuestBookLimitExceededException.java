package com.ottention.banana.exception;

public class GuestBookLimitExceededException extends Exception {

    public static final String MESSAGE = "타인 명함의 방명록은 최대 4개 작성할 수 있습니다.";

    public GuestBookLimitExceededException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 422;
    }

}
