package de.ait.platform.user.service;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.reposittory.UserRepository;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    UserResponseDto deleteUser(Long id);
    List<UserResponseDto> getUser();
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getUserByTitle(String title);


}
