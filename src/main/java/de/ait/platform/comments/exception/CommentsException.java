package de.ait.platform.comments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CommentsException {

    @ExceptionHandler(CommentNotFound.class)
    public ResponseEntity<String> handleCommentsNotFound(CommentNotFound ex, WebRequest request) {
        // Змінюємо статус на 404 (NOT_FOUND)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        // Змінюємо статус на 400 (BAD_REQUEST) для інших винятків
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
