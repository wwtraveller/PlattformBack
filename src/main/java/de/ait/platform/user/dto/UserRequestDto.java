package de.ait.platform.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String photo;

    public UserRequestDto(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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