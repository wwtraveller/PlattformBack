package de.ait.platform.user.dto;

import de.ait.platform.role.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String photo;

    private Set<Role> roles;

    public UserResponseDto(Long id, String username, String email, Set<Role> roles) {


    }
}

