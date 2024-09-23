package de.ait.platform.category.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.exceptions.CategoryNotFound;
import de.ait.platform.category.repository.CategoryRepository;
import de.ait.platform.role.entity.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private final ModelMapper mapper;
    private final CategoryRepository repository;

    private final ArticleRepository articleRepository;

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
            }
        } else {
            throw new CategoryNotFound("Category with id:" + categoryId + " not found");
        }
        return List.of();
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
        Category entity = mapper.map(categoryDTO, Category.class);
        Category newCategory = repository.save(entity);
        return mapper.map(newCategory, CategoryResponse.class);
    }

    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryDTO) {
        Category existingCategory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        if (categoryDTO.getName() != null && !categoryDTO.getName().isEmpty()) {
            existingCategory.setName(categoryDTO.getName());
        }
        Category updatedCategory = repository.save(existingCategory);

        return mapper.map(updatedCategory, CategoryResponse.class);

    }
}
