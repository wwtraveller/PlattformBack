package de.ait.platform.user.service;

import de.ait.platform.user.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User deleteUser(User user);
    User updateUser(User user);
    User findUserById(Long id);
    List<User> findUserByName(String firstName, String lastName);
}
