package com.accenture.accountmanagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(InsufficientBalanceException ex) {
        logger.warn("Insufficient balance: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.UNPROCESSABLE_CONTENT.value());
        error.put("error", "Insufficient balance");
        error.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        logger.warn("Runtime Exception: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Not found");
        error.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());
        Map<String, Object> fieldErrors = new HashMap<>();

        // get the list fof all fields that failed validation
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("Timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("error", "Validation failed.");
        response.put("messages", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
