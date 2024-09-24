package de.ait.platform.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDto {

    @Schema(description = "User name", example = "john_doe")
    private String username;

    @Schema(description = "User password", example = "password123", minLength = 8)
    private String password;
}
