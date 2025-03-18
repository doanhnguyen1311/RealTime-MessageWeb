package com.example.identity_service.exception;

import com.example.identity_service.dto.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleException(ResponseStatusException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .time(LocalDateTime.now())
                .code(ex.getStatusCode().value())
                .message(ex.getReason())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
