package com.totm.totm.exception;

public class GameNotClosedException extends RuntimeException {
    public GameNotClosedException(String message) {
        super(message);
    }
}
