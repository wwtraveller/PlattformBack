package de.ait.platform.user.service;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserPhotoUrlDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService{
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    UserResponseDto deleteUser(Long id);
    List<UserResponseDto> getUsers();
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getUserByEmail(String email);
    UserResponseDto setAdminRole(String email, boolean admin);
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
    public String addPhotoByUrl(UserPhotoUrlDto photoUrlDto);
    public String addPhotoByFile(MultipartFile photoFile);



}
