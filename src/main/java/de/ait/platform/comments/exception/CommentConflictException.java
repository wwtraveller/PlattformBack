package de.ait.platform.comments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CommentConflictException extends RuntimeException {
     public CommentConflictException(String message) {
          super(message);
     }
}
