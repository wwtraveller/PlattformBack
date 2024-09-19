package de.ait.platform.category.controller;

import de.ait.platform.article.entity.Article;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/categories/{id}")
    public CategoryResponse getCategoryById(@PathVariable(name="id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/categories")
    public CategoryResponse createCategory(@RequestBody CategoryRequest dto) {
        return service.save(dto);
    }

    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(@RequestBody CategoryRequest dto, @PathVariable Long id) {
        return service.update(id, dto);
    }

    @DeleteMapping("/categories/{id}")
    public CategoryResponse deleteCategory(@PathVariable Long id) {
        return service.delete(id);
    }

    @PutMapping("/categories/{categoryId}/articles/{articleId}")
    public List<Article> addArticleToCategory(@PathVariable Long articleId,
                                              @PathVariable Long categoryId) {
        return service.addArticleToCategory(articleId, categoryId);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategory(@RequestParam(name="title", required = false, defaultValue = "") String title){
        if (title.isEmpty()) {
            return service.findAll();
        } else {
            return service.findByName(title);
        }
    }
}
