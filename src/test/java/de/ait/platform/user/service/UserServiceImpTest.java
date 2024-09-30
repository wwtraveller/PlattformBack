package de.ait.platform.user.service;

import de.ait.platform.role.entity.Role;
import de.ait.platform.role.service.RoleService;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.reposittory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImpTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImp userService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private RoleService roleService;
    @Mock
    private BCryptPasswordEncoder encoder;

    private User user;
    private Role role;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setTitle("ROLE_USER");

        user = new User(1L, "testuser", "Test", "User", "test@example.com", "password", "photoUrl", new HashSet<>());

        userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("username");
        userRequestDto.getEmail();
        userRequestDto.setPassword("password");

        userResponseDto = new UserResponseDto();

    }

    @Test
    void UserService_CreateUser_Success() {
        when(repository.findUserByUsername("testuser")).thenReturn(Optional.empty());
        when(roleService.getRoleByTitle("ROLE_USER")).thenReturn(role);
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(mapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());

        UserResponseDto savedUser = userService.createUser(userRequestDto);
        assertNotNull(savedUser);
        verify(repository, times(1)).save(any(User.class));

    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void setAdminRole() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void isUsernameTaken() {
    }

    @Test
    void isEmailTaken() {
    }
}