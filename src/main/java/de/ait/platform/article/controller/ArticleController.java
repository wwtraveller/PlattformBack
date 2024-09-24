package de.ait.platform.article.controller;

import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {
    private final ArticleService service;

    @GetMapping("/articles/{id}")
    private ResponseArticle findById(@PathVariable Long id) {
        return service.fingById(id);
    }

    @PostMapping("/articles")
    public ResponseArticle addArticle(@RequestBody RequestArticle article) {
        return service.createArticle(article);
    }

    @PutMapping("/articles/{id}")
    public ResponseArticle updateArticle(@PathVariable Long id, @RequestBody RequestArticle article) {
        return service.updateArticle(id, article);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseArticle deleteArticle(@PathVariable Long id) {
        return service.deleteArticle(id);
    }

    @GetMapping("/articles")
    public List<ResponseArticle> getArticle(@RequestParam(name="title", required = false, defaultValue = "") String title){
        if (title.isEmpty()) {
            return service.fingAll();
        } else {
            return service.fingByTitle(title);
        }
    }
}
