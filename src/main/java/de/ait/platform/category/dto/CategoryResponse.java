package de.ait.platform.category.dto;

import de.ait.platform.article.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for category response")
public class CategoryResponse {

    @Schema(description = "Unique identifier of the category", example = "1", required = true)
    private Long id;

    @Schema(description = "Name of the category", example = "Technology", required = true)
    private String name;

    @Schema(description = "List of articles associated with the category")
    private List<Article> articles;
}
