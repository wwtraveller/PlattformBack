package de.ait.platform.category.dto;

import de.ait.platform.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<Article> articles;
}
