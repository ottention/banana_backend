package com.ottention.banana.exception;

public class QRCodeGenerationException extends Exception {

    private static final String MESSAGE = "QR코드 생성 중 에러가 발생했습니다.";

    public QRCodeGenerationException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
