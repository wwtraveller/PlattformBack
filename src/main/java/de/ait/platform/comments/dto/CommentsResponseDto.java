package de.ait.platform.comments.dto;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentsResponseDto {
    private Long id;
    private String text;
    private UserResponseDto user;
    private ResponseArticle article;
}

