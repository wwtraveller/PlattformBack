package de.ait.platform.category.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.FieldIsBlank;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.exceptions.CategoryNotFound;
import de.ait.platform.category.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImp implements CategoryService {
    private final ModelMapper mapper;
    private final CategoryRepository repository;

    private final ArticleRepository articleRepository;
@Autowired
    public CategoryServiceImp(ArticleRepository articleRepository, CategoryRepository repository, ModelMapper mapper) {
        this.articleRepository = articleRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = repository.findAll();
        return categories.stream()
                .map(c -> mapper.map(c, CategoryResponse.class))
                .toList();
    }

    @Transactional
    @Override
    public CategoryResponse findById(Long id) {
        String message = "Couldn't find category with id:" + id;
        Category foundCategory = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFound(message));
        return mapper.map(foundCategory, CategoryResponse.class);
    }

    @Transactional
    @Override
    public List<CategoryResponse> findByName(String name) {
        String message = "Couldn't find category with name:" + name;
        List<Category> foundCategory = repository.findByName(name);
        if (foundCategory == null) {
            throw new CategoryNotFound(message);
        } else {
            return foundCategory.stream()
                    .map(c -> mapper.map(c, CategoryResponse.class))
                    .toList();
        }
    }

    @Transactional
    @Override
    public List<Article> addArticleToCategory(Long articleId, Long categoryId) {
        Optional<Category> category = repository.findById(categoryId);
        Optional<Article> article = articleRepository.findById(articleId);
        if (category.isPresent()) {
            if (article.isPresent()) {
                return category.get().addArticle(mapper.map(article, Article.class));
            } else {
                throw new CategoryNotFound("Error article with id  " + articleId + " not found.");
            }
        } else {
            throw new CategoryNotFound("Category with id:" + categoryId + " not found");
        }

    }

    @Transactional
    @Override
    public CategoryResponse delete(Long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new CategoryNotFound("Error deleting category. Couldn't find category with id:" + id);
        }
        return mapper.map(category, CategoryResponse.class);
    }

    @Transactional
    @Override
    public CategoryResponse save(CategoryRequest categoryDTO) {
        if (!categoryDTO.getName().isBlank()) {
            if (repository.findByName(categoryDTO.getName()).isEmpty()) {
                Category entity = mapper.map(categoryDTO, Category.class);
                Category newCategory = repository.save(entity);
                return mapper.map(newCategory, CategoryResponse.class);
            } else {
                throw new CategoryNotFound("Error Category with name " + categoryDTO.getName() + " is taken");
            }
        } else {
            throw new CategoryNotFound("Error. Category name cannot be empty");
        }
    }

    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryDTO) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        if (!categoryDTO.getName().isBlank()) {

            if (repository.findByName(categoryDTO.getName()).isEmpty()) {
                existingCategory.setName(categoryDTO.getName());
            } else {
                throw new CategoryNotFound("Error Category with name " + categoryDTO.getName() + " is taken");
            }
        } else {
            throw new CategoryNotFound("Error. Category name cannot be empty");
        }
        Category updatedCategory = repository.save(existingCategory);

        return mapper.map(updatedCategory, CategoryResponse.class);
    }
@Transactional
@Override
    public List<ResponseArticle> findArticleInCategory(String name, String title){
    List<ResponseArticle> foundedArticles = new ArrayList<>();
    if(name.isEmpty()|| title.isEmpty()||name.isBlank()||title.isBlank()){
        throw new FieldIsBlank("Title oder name is uncorrected");
    }
    List<CategoryResponse> categoryResponse = findByName(name);
    for(CategoryResponse category : categoryResponse){
        List<Article> articles = category.getArticles();
        for(Article article : articles){
            if(article.getTitle().contains(title)){
                foundedArticles.add(mapper.map(article, ResponseArticle.class));
            }

        }
    }
    return foundedArticles;
    }
@Transactional
    @Override
    public List<ResponseArticle> findArticleInCategories( String title){
        List<ResponseArticle> foundedArticles = new ArrayList<>();
        if( title.isEmpty()||title.isBlank()){
            throw new FieldIsBlank("Title oder name is uncorrected");
        }
        List<CategoryResponse> categoryResponse = findAll();
        for(CategoryResponse category : categoryResponse){
            List<Article> articles = category.getArticles();
            for(Article article : articles){
                if(article.getTitle().contains(title)){
                    foundedArticles.add(mapper.map(article, ResponseArticle.class));
                }
            }
        }
        return foundedArticles;
    }
}
