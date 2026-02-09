package com.ecom.exceptions;

public class ApiException extends RuntimeException{
    private final Long exceptionId = 1L;

    public ApiException() {
    }
    public ApiException(String message) {
        super(message);
    }
}
