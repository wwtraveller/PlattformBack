package de.ait.platform.article.service;

import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;

import java.util.List;

public interface ArticleService {
    List<ResponseArticle> fingAll();
    ResponseArticle fingById(Long id);
    List<ResponseArticle> fingByTitle(String title);
    ResponseArticle createArticle(RequestArticle article);
    ResponseArticle updateArticle(Long id, RequestArticle article);
    ResponseArticle deleteArticle(Long id);

}
