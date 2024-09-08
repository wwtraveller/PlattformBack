package de.ait.platform.article.exception;

public class ArticleNotFound extends RuntimeException {
    public ArticleNotFound(String message) {
        super(message);
    }
}
