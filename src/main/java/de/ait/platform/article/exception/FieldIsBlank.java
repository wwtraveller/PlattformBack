package de.ait.platform.article.exception;

public class FieldIsBlank extends RuntimeException {
    public FieldIsBlank(String message) {
        super(message);
    }
}
