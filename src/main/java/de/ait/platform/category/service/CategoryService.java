package de.ait.platform.category.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Article> addArticleToCategory(Long articleId, Long categoryId);
    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    List<CategoryResponse> findByName(String name);
    CategoryResponse delete(Long id);
    CategoryResponse save(CategoryRequest categoryDTO);
    CategoryResponse update(Long id, CategoryRequest categoryDTO);
    List<ResponseArticle> findArticleInCategories( String title);
    List<ResponseArticle> findArticleInCategory(String name, String title);
}
