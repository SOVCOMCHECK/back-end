package ru.sovcomcheck.back_end.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handle(Exception ex) {

        return ResponseEntity.badRequest().build();

    }
}
