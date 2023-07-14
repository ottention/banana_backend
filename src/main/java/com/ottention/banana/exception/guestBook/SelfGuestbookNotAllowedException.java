package com.ottention.banana.exception.guestBook;

import com.ottention.banana.exception.Exception;

public class SelfGuestbookNotAllowedException extends Exception {

    private static final String MESSAGE = "자기 자신의 명함에는 방명록을 남길 수 없습니다.";

    public SelfGuestbookNotAllowedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 403;
    }

}
