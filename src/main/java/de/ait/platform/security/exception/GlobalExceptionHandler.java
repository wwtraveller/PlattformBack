package de.ait.platform.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик для AuthException
    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<String> handleAuthException(CustomAuthException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // Обработчик для TokenNotFound
    @ExceptionHandler(TokenNotFound.class)
    public ResponseEntity<String> handleTokenNotFoundException(TokenNotFound ex, WebRequest request) {
        return new ResponseEntity<>("Token not found: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

     // Обработчик для других исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}