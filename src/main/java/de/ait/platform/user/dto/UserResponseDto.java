package de.ait.platform.user.dto;

import de.ait.platform.role.entity.Role;
import de.ait.platform.user.comments.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

public class UserResponseDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class User {

        private Long id;

        private String firstName;

        private String lastName;

        private String email;

        private String photo;

    }
}
