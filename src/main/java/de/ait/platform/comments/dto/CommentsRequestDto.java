package de.ait.platform.comments.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for creating or updating a comment")
public class CommentsRequestDto {

    @Schema(description = "Text of the comment", example = "This is an insightful article", required = true)
    private String text;

    @Schema(description = "ID of the user who created the comment", example = "10", required = true)
    private Long user_id;

    @Schema(description = "ID of the article to which the comment belongs", example = "5", required = true)
    private Long article_id;
}
