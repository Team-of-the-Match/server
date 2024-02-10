package com.totm.totm.exception;

public class PasswordsNotEqualException extends RuntimeException {
    public PasswordsNotEqualException(String message) {
        super(message);
    }
}
