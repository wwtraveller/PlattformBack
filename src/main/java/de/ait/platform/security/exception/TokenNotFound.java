package de.ait.platform.security.exception;

public class TokenNotFound extends RuntimeException{
    public TokenNotFound(String message){
        super(message);
    }
}
