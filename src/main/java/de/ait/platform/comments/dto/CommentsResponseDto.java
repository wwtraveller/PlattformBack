package de.ait.platform.comments.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for the response containing comment details")
public class CommentsResponseDto {

    @Schema(description = "Unique identifier of the comment", example = "1", required = true)
    private Long id;

    @Schema(description = "Text of the comment", example = "Great article!", required = true)
    private String text;

    @Schema(description = "ID of the user who posted the comment", example = "10", required = true)
    private Long userId;

    @Schema(description = "ID of the article the comment is associated with", example = "5", required = true)
    private Long articleId;
}

