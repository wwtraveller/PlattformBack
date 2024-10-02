package de.ait.platform.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO for creating or updating a category")
public class CategoryRequest {

    @Schema(description = "Name of the category", example = "Technology", required = true)
    private String name;
}
