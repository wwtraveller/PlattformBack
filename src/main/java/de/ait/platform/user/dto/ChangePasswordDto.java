package de.ait.platform.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChangePasswordDto {
    @Schema(description = "old password", example = "pasSword123 ", minLength = 8)
    private String oldPassword;

    @Schema(description = "new password", example = "pasSword125 ", minLength = 8)
    private String newPassword;

    @Schema(description = "repeat", example = "pasSword125")
    private String repeatPassword;
}
