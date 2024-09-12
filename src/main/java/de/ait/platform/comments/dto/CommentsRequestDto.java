package de.ait.platform.comments.dto;
import lombok.*;
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class CommentsRequestDto {

    private String text;
    private Long user_id;
    private Long article_id;
}
