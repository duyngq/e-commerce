package com.store.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExceptionDetails {
    private int statusCode;
    private String message;
    private String details;
    private Map<String, String> errors;
    public ExceptionDetails(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

    public ExceptionDetails(int statusCode, String message ) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
