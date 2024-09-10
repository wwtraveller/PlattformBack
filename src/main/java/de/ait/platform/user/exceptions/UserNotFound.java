package de.ait.platform.user.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound (String message) {
        super(message);
    }
}
