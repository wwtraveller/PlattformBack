package de.ait.platform.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ait.platform.article.entity.Article;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable( name="categories_articles",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="article_id")
    )
    @JsonIgnore
    private List<Article> articles = new ArrayList<>();

    public List<Article> addArticle(Article article) {
        articles.add(article);
        return articles;
    }
}
