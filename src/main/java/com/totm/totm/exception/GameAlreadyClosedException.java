package com.totm.totm.exception;

public class GameAlreadyClosedException extends RuntimeException {
    public GameAlreadyClosedException(String message) {
        super(message);
    }
}
