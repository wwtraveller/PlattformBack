package de.ait.platform.article.dto;

import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseArticle {
    private Long id;
    private String title;
    private String content;
    private String photo;
    private Set<Comment> comments;
    private String username;
}
