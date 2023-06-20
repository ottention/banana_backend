package com.ottention.banana.controller;

import com.ottention.banana.dto.response.error.BindErrorResponse;
import com.ottention.banana.exception.Exception;
import com.ottention.banana.dto.response.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        int statusCode = e.getStatus();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public BindErrorResponse BindException(BindException bindException) {
        List<FieldError> errors = bindException.getFieldErrors();

        return BindErrorResponse.builder()
                .message(errors.stream()
                        .map(e -> e.getDefaultMessage())
                        .collect(Collectors.toList()))
                .build();
    }

}
