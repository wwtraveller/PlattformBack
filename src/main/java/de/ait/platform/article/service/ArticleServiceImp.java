package de.ait.platform.article.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@AllArgsConstructor
@Service
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<RequestArticle> fingAll() {
        List<Article> list = repository.findAll();
        return list.stream()
                .map(article -> mapper.map(article, RequestArticle.class))
                .toList();
    }

    @Override
    public RequestArticle fingById(Long id) {
        Optional<Article> article = repository.findById(id);
        if (article.isPresent()) {
            return mapper.map(article.get(), RequestArticle.class);
        }
        else {
            String message = "Article with id: " + id + " not found";
            throw new ArticleNotFound(message);
        }
    }

    @Override
    public List<RequestArticle> fingByTitle(String title) {
        Predicate<Article> predicateByTitle =
                (title.equals("")) ? a-> true:  article -> article.getTitle().equalsIgnoreCase(title);
        List<Article> articleList = repository.findAll().stream().filter(predicateByTitle).toList();
        return articleList.stream().map(article -> mapper.map(article, RequestArticle.class)).toList();
    }

    @Override
    public RequestArticle createArticle(ResponseArticle dto) {
        repository.save(mapper.map(dto, Article.class));
        return mapper.map(dto, RequestArticle.class);
    }

    @Override
    public RequestArticle updateArticle(Long id, ResponseArticle dto) {
        Article article = mapper.map(dto, Article.class);
        article.setId(id);
        Article entity = repository.save(article);
        return mapper.map(entity, RequestArticle.class);
    }

    @Override
    public RequestArticle deleteArticle(Long id) {
        Optional<Article> foundedArticle = repository.findById(id);
        repository.deleteById(id);
        return mapper.map(foundedArticle, RequestArticle.class);
    }
}
