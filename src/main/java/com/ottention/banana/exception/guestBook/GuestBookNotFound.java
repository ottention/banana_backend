package com.ottention.banana.exception.guestBook;

import com.ottention.banana.exception.Exception;

public class GuestBookNotFound extends Exception {

    private static final String MESSAGE = "존재하지 않는 방명록입니다.";

    public GuestBookNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }

}
