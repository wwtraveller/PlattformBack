package de.ait.platform.article.controller;

import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.exception.ArticleExceptions;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService service;

    @Operation(summary = "Get article by ID", description = "Retrieves an article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseArticle.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content)
    })
    @GetMapping("/articles/{id}")
    public ResponseArticle findById(@Parameter(description = "ID of the article to retrieve") @PathVariable Long id) {
        try {
            return service.findById(id);
        }
        catch (ArticleNotFound e){
            throw new ArticleNotFound("Article with id: " + id + " not found");
        }
    }

    @Operation(summary = "Create a new article", description = "Adds a new article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseArticle.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/articles")
    public ResponseEntity<ResponseArticle> addArticle(@RequestBody RequestArticle requestArticle) {
        ResponseArticle responseArticle = service.createArticle(requestArticle);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseArticle);
    }
//    @PostMapping("/articles")
//    public ResponseArticle addArticle(@RequestBody RequestArticle article) {
//            return service.createArticle(article);
//    }

    @Operation(summary = "Update article by ID", description = "Updates an existing article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseArticle.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content)
    })

    @PutMapping("/articles/{id}")
    public ResponseArticle updateArticle(@Parameter(description = "ID of the article to update") @PathVariable Long id,
                                         @RequestBody RequestArticle article) {
        return service.updateArticle(id, article);
    }

    @Operation(summary = "Delete article by ID", description = "Deletes an article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseArticle.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content)
    })
    @DeleteMapping("/articles/{id}")
    public ResponseArticle deleteArticle(@Parameter(description = "ID of the article to delete") @PathVariable Long id) {
        try {
            return service.deleteArticle(id);
        }
        catch (ArticleNotFound e){
            throw new ArticleNotFound("Article with id: " + id + " not found");
        }

    }

    @Operation(summary = "Get all articles", description = "Retrieves a list of all articles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of articles retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseArticle.class)))
    })
    @GetMapping("/articles")
    public List<ResponseArticle> getArticle(@RequestParam(name="title", required = false, defaultValue = "") String title) {
        if (title.isEmpty()) {
            return service.findAll();
        }
        else {
            return service.findByTitle(title);
        }
    }

}
