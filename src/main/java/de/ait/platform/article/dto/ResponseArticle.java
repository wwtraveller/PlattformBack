package de.ait.platform.article.dto;

import de.ait.platform.category.entity.Category;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO for the response containing article details")
public class ResponseArticle {

    @Schema(description = "Unique identifier of the article", example = "1", required = true)
    private Long id;

    @Schema(description = "Title of the article", example = "Getting Started with Spring Boot", required = true)
    private String title;

    @Schema(description = "Content of the article", example = "This article explains the basics of Spring Boot...")
    private String content;

    @Schema(description = "URL to the article's photo", example = "https://example.com/photo.jpg")
    private String photo;

    @Schema(description = "Username of the article's author", example = "john_doe")
    private String username;

    @Schema(description = "Set of comments associated with the article")
    private Set<Comment> comments;

    @Schema(description = "Set of categories associated with the article")
    private Set<Category> categories;

}
