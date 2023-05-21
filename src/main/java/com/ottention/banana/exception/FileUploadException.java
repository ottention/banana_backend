package com.ottention.banana.exception;

public class FileUploadException extends Exception {

    private static final String MESSAGE = "파일 업로드 중 예외가 발생했습니다.";

    public FileUploadException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 500;
    }
}
