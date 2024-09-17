package de.ait.platform.security;

public class TokenNotFound extends RuntimeException{
    public TokenNotFound(String message){
        super(message);
    }
}
