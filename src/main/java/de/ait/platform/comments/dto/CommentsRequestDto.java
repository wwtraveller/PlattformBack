package de.ait.platform.comments.dto;

import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class CommentsRequestDto {

    private String text;
    private Long userId;
    private Long articleId;

    public CommentsRequestDto(Long articleId, Long userId, String text) {
        this.articleId = articleId;
        this.userId = userId;
        this.text = text;
    }

}
