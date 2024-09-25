package de.ait.platform.article.exception;

public class FieldIsTaken extends RuntimeException {
    public FieldIsTaken(String message) {
        super(message);
    }
}