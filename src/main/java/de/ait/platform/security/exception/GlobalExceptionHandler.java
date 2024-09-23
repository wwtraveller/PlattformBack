package de.ait.platform.security.exception;

import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик для AuthException
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(CustomAuthException ex, WebRequest request) {
        return new ResponseEntity<>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Обработчик для TokenNotFound
    @ExceptionHandler(TokenNotFound.class)
    public ResponseEntity<String> handleTokenNotFoundException(TokenNotFound ex, WebRequest request) {
        return new ResponseEntity<>("Token not found: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Обработчик для других исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}