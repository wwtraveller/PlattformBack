package de.ait.platform.category.controller;

import de.ait.platform.article.entity.Article;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService service;

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/categories/{id}")
    public CategoryResponse getCategoryById(@Parameter(description = "ID of the category to retrieve") @PathVariable(name="id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new category", description = "Adds a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/categories")
    public CategoryResponse createCategory(@RequestBody CategoryRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Update category by ID", description = "Updates an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(@RequestBody CategoryRequest dto, @Parameter(description = "ID of the category to update") @PathVariable Long id) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete category by ID", description = "Deletes a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @DeleteMapping("/categories/{id}")
    public CategoryResponse deleteCategory(@Parameter(description = "ID of the category to delete") @PathVariable Long id) {
        return service.delete(id);
    }

    @Operation(summary = "Add article to category", description = "Associates an article with a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article added to category",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Article.class))),
            @ApiResponse(responseCode = "404", description = "Category or article not found", content = @Content)
    })
    @PutMapping("/categories/{categoryId}/articles/{articleId}")
    public List<Article> addArticleToCategory(@Parameter(description = "ID of the article to add") @PathVariable Long articleId,
                                              @Parameter(description = "ID of the category to add the article to") @PathVariable Long categoryId) {
        return service.addArticleToCategory(articleId, categoryId);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class)))
    })
    @GetMapping("/categories")
    public List<CategoryResponse> getCategory(@RequestParam(name="title", required = false, defaultValue = "") String title) {
        if (title.isEmpty()) {
            return service.findAll();
        } else {
            return service.findByName(title);
        }
    }
}
