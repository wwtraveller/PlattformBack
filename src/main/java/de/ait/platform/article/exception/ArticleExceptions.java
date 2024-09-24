package de.ait.platform.article.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ArticleExceptions {
    @ExceptionHandler(ArticleNotFound.class)
    public ResponseEntity<String> handleArticleNotFound(ArticleNotFound ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(FieldIsBlank.class)
    public ResponseEntity<String> handleTitleIsBlank(FieldIsBlank ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(FieldCannotBeNull.class)
    public ResponseEntity<String> handleNullField(FieldCannotBeNull ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(FieldIsTaken.class)
    public ResponseEntity<String> handleFieldIsTaken(FieldIsTaken ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}