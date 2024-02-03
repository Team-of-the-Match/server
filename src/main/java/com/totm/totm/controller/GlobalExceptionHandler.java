package com.totm.totm.controller;

import com.totm.totm.dto.ErrorResponse;
import com.totm.totm.exception.DuplicatedMemberException;
import com.totm.totm.exception.MemberNotFoundException;
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
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(500))
                .body(ErrorResponse.builder().status(500).message(e.getMessage()).build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(500))
                .body(ErrorResponse.builder().status(500).message(e.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(ErrorResponse.builder().status(400).message(errors.get(0)).build());
    }

    @ExceptionHandler({ MemberNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(ErrorResponse.builder().status(404).message(e.getMessage()).build());
    }

    @ExceptionHandler(DuplicatedMemberException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicatedMemberException(DuplicatedMemberException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(409))
                .body(ErrorResponse.builder().status(409).message(e.getMessage()).build());
    }
}
