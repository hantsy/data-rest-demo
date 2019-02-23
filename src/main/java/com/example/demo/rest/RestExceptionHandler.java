package com.example.demo.rest;

import com.example.demo.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.notFound;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler()
    public ResponseEntity postNotFound(PostNotFoundException ex, WebRequest request) {
        log.debug("handling PostNotFoundException:{}" + ex.getMessage());
        return notFound().build();
    }
}
