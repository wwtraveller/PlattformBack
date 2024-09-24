package de.ait.platform.user.dto;

import de.ait.platform.role.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    @Schema(description = "Username for enter system", example = "john_doe" )
    private String username;

    @Schema(description = "username", example = "John")
    private String firstName;

    @Schema(description = "Lastname", example = "Doe")
    private String lastName;

    @Schema(description = "Email", example = "johndoe@email.com")
    private String email;

    @Schema(description = "password", example = "password123")
    private String password;

    @Schema(description = "photo user",example = "photo.jpg")
    private String photo;

    @Schema(description = "User role")
    private Set<Role> roles;

    public UserRequestDto(String username, String firstName, String lastName, String email, String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserRequestDto(String username, String firstName, String lastName, String email, String password, String photo) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.photo = "";
    }

    public UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}