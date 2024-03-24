package com.totm.totm.controller;

import com.totm.totm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors.get(0));
    }

    @ExceptionHandler({ PasswordNotEqualException.class, ValueNotEqualException.class, MemberStopException.class })
    public ResponseEntity<String> handleUnauthorizedException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({ MemberNotFoundException.class, CommentNotFoundException.class, NotificationNotFoundException.class,
                        PostNotFoundException.class, ScoreNotFoundException.class, ManagerNotFoundException.class,
                        GameNotFoundException.class, PredictionNotFoundException.class, TokenNotFoundException.class })
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({ AlreadyLikedException.class, NeverLikedException.class, GameAlreadyExistException.class,
                        PredictionAlreadyExistException.class, GameAlreadyClosedException.class, GameNotClosedException.class,
                        ScoreAlreadyUpdatedException.class, MemberUnconfirmedException.class, ValueExpiredException.class })
    public ResponseEntity<String> handleNotAcceptableException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(406)).body(e.getMessage());
    }

    @ExceptionHandler({ DuplicatedMemberException.class, DuplicatedEmailException.class, DuplicatedUsernameException.class })
    public ResponseEntity<String> handleConflictException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(e.getMessage());
    }
}
