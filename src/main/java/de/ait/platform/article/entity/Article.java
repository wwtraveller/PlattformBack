package de.ait.platform.article.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.ait.platform.category.entity.Category;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "photo")
    private String photo;

    @ManyToMany
    @JoinTable(
            name = "categories_articles",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
//    @JsonManagedReference
    private Set<Category> categories;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    //@JsonIgnore
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonProperty("user")
    public String getUserUsername() {
        return user.getUsername();
    }
}
