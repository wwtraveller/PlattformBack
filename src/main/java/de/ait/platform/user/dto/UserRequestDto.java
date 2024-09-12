package de.ait.platform.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
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



}