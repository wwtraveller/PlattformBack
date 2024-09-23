package de.ait.platform.article.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@AllArgsConstructor
@Service
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository repository;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public List<ResponseArticle> fingAll() {
        List<Article> list = repository.findAll();
        return list.stream()
                .map(article -> mapper.map(article, ResponseArticle.class))
                .toList();
    }

    @Transactional
    @Override
    public ResponseArticle fingById(Long id) {
        Optional<Article> article = repository.findById(id);
        if (article.isPresent()) {
            return mapper.map(article.get(), ResponseArticle.class);
        }
        else {
            String message = "Article with id: " + id + " not found";
            throw new ArticleNotFound(message);
        }
    }

    @Transactional
    @Override
    public List<ResponseArticle> fingByTitle(String title) {
        Predicate<Article> predicateByTitle =
                (title.equals("")) ? a-> true:  article -> article.getTitle().equalsIgnoreCase(title);
        List<Article> articleList = repository.findAll().stream().filter(predicateByTitle).toList();
        return articleList.stream().map(article -> mapper.map(article, ResponseArticle.class)).toList();
    }


    @Override
    public ResponseArticle createArticle(RequestArticle dto) {
        Article entity = mapper.map(dto, Article.class);
        repository.save(entity);
        return mapper.map(entity, ResponseArticle.class);
    }

    @Transactional
    @Override
    public ResponseArticle updateArticle(Long id, RequestArticle dto) {
        Article existingArticle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            existingArticle.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null && !dto.getContent().isEmpty()) {
            existingArticle.setContent(dto.getContent());
        }
        if (dto.getComments() != null && !dto.getComments().isEmpty()) {
            existingArticle.setComments(dto.getComments());
        }
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            existingArticle.setPhoto(dto.getPhoto());
        }

        Article updatedArticle = repository.save(existingArticle);

        return mapper.map(updatedArticle, ResponseArticle.class);
    }

    @Transactional
    @Override
    public ResponseArticle deleteArticle(Long id) {
        Optional<Article> foundedArticle = repository.findById(id);
        repository.deleteById(id);
        return mapper.map(foundedArticle, ResponseArticle.class);
    }
}
