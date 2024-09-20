package de.ait.platform.article.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestArticle {
    private String title;
    private String content;
    @Nullable
    private String photo;
    @Nullable
    private Set<Comment> comments;
    @Nullable
    private User user;
}