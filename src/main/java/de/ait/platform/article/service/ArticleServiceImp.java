package de.ait.platform.article.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.service.CategoryService;
import de.ait.platform.category.service.CategoryServiceImp;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;


@AllArgsConstructor
@Service
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository repository;
    private final CategoryServiceImp categoryService;
    private final ModelMapper mapper;
    private final AuthService service;


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
        UserResponseDto userDto = service.getAuthenticatedUser();
        Set<Category> categories = new HashSet<>();
        if (dto.getCategories() != null) {
            for (Number number: dto.getCategories()){
                CategoryResponse categoryResponse = categoryService.findById(number.longValue());
                Category category = mapper.map(categoryResponse, Category.class);
                categories.add(category);
            }
        }

        User user = mapper.map(userDto, User.class);
        entity.setCategories(categories);
        entity.setUser(user);
        repository.save(entity);
        return new ResponseArticle(entity.getId(),entity.getTitle(),entity.getContent(),
                entity.getPhoto(),entity.getUser().getUsername(),entity.getComments(), entity.getCategories());
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
        if (foundedArticle.isPresent()) {

            Set<Comment> comments = foundedArticle.get().getComments();
            for (Comment comment : comments) {
                comment.setArticle(null);
                comment.setUser(null);
            }
            foundedArticle.get().setComments(new HashSet<>());
        }
        repository.deleteById(id);
        return mapper.map(foundedArticle, ResponseArticle.class);
    }
}
