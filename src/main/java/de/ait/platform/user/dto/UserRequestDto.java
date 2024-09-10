package de.ait.platform.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String photo;

    public UserRequestDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photo = "https://img.favpng.com/20/8/6/computer-icons-business-facebook-bank-symbol-png-favpng-5S9wcfPXkrmFfNr5x9ASw1BH9.jpg";

    }
}