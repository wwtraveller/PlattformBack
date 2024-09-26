package de.ait.platform.comments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CommentForbiddenException extends RuntimeException {
     public CommentForbiddenException(String message) {
          super(message);
     }}
