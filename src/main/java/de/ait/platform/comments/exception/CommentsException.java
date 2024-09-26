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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CommentConflictException.class)
    public ResponseEntity<String> handleCommentConflict(CommentConflictException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());  // Статус 409 - CONFLICT
    }

    @ExceptionHandler(CommentForbiddenException.class)
    public ResponseEntity<String> handleCommentForbidden(CommentForbiddenException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());  // Статус 403 - FORBIDDEN
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
