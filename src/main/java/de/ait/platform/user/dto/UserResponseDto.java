package de.ait.platform.user.dto;

import de.ait.platform.role.entity.Role;
import de.ait.platform.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {

    @Schema(description = "Unique identifier of user", example = "1")
    private Long id;

    @Schema(description = "Username for login", example = "john_doe")
    private String username;

    @Schema(description = "First name of the user\", example = John")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description ="Email address of the user", example = "john.doe@example.com" )
    private String email;

    @Schema(description ="Photo of the user (URL or file name)", example = "profile.jpg" )
    private String photo;

    @Schema(description = "Set of roles assigned to the user")
    private Set<Role> roles;

}


