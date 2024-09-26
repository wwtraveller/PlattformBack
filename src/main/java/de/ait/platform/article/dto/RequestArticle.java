package de.ait.platform.article.dto;

import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for creating or updating an article")
public class RequestArticle {

    @Schema(description = "Title of the article", example = "Introduction to Spring Framework", required = true)
    private String title;

    @Schema(description = "Content of the article", example = "Spring is a powerful framework...")
    private String content;

    @Nullable
    @Schema(description = "URL to the article's photo", example = "https://example.com/photo.jpg")
    private String photo;


    @Nullable
    @Schema(description = "Set of comments associated with the article")
    private Set<Comment> comments;

    @Nullable
    @Schema(description = "User who created the article")
    private User user;


    @Nullable
    @Schema(description = "Set of category IDs associated with the article", example = "[1, 2, 3]")
    private Set<Long> categories;
}