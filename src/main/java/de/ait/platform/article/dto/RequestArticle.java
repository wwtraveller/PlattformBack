package de.ait.platform.article.dto;

import de.ait.platform.user.comments.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
public class RequestArticle {
    private String title;
    private String content;
    private String photo;
    private Set<Comment> comments;

    public RequestArticle(String title, String content, String photo) {
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.comments = new HashSet<>();
    }
    public RequestArticle(String title, String content) {
        this.title = title;
        this.content = content;
        this.photo = "https://img.favpng.com/20/8/6/computer-icons-business-facebook-bank-symbol-png-favpng-5S9wcfPXkrmFfNr5x9ASw1BH9.jpg";
        this.comments = new HashSet<>();
    }
}
