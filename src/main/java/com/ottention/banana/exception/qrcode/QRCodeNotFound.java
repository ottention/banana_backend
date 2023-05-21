package com.ottention.banana.exception.qrcode;

import com.ottention.banana.exception.Exception;

public class QRCodeNotFound extends Exception {

    private static final String MESSAGE = "존재하지 않는 QR코드 입니다.";

    public QRCodeNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}
