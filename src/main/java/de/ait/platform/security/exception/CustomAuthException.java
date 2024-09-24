package de.ait.platform.security.exception;


public class CustomAuthException extends RuntimeException{
    public CustomAuthException (String message) {
        super(message);
    }
}
