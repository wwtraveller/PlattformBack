package de.ait.platform.comments.dto;
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
    private Long userId;
    private Long articleId;
}

