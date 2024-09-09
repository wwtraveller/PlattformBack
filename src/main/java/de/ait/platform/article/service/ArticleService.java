package de.ait.platform.article.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.dto.RequestArticle;

import java.util.List;

public interface ArticleService {
    List<RequestArticle> fingAll();
    RequestArticle fingById(Long id);
    List<RequestArticle> fingByTitle(String title);
    RequestArticle createArticle(ResponseArticle article);
    RequestArticle updateArticle(Long id, ResponseArticle article);
    RequestArticle deleteArticle(Long id);

}
