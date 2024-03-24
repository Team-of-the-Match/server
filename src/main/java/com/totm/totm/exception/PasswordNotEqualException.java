package com.totm.totm.exception;

public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException(String message) {
        super(message);
    }
}
