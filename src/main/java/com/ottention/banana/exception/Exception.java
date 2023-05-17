package com.ottention.banana.exception;

public abstract class Exception extends RuntimeException {

    public Exception(String message) {
        super(message);
    }

    public abstract int getStatus();
}
