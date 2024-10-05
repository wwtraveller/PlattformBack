package de.ait.platform.user.service;

import de.ait.platform.role.entity.Role;
import de.ait.platform.role.service.RoleService;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void UserServiceImp_CreateUser_UserResponseDto() {
        // Given: подготавливаем тестовые данные
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("testuser");
        userRequestDto.setPassword("password123");
        userRequestDto.setEmail("testuser@example.com");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedPassword123");
        user.setEmail("testuser@example.com");

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername("testuser");
        userResponseDto.setEmail("testuser@example.com");

        Role defaultRole = new Role();
        defaultRole.setId(1L);  // Устанавливаем значение id
        defaultRole.setTitle("ROLE_USER");

        // Мокируем методы
        when(roleService.getRoleByTitle("ROLE_USER")).thenReturn(defaultRole);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(userResponseDto);

        // When: вызываем метод createUser
        UserResponseDto createdUser = userService.createUser(userRequestDto);

        // Then: проверяем результат
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("testuser@example.com", createdUser.getEmail());

        // Проверяем, что зависимости были вызваны корректное количество раз
        verify(roleService, times(1)).getRoleByTitle("ROLE_USER");
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(any(User.class), eq(UserResponseDto.class));

    }

    @Test
    void UserServiceImp_UpdateUser_success() {
        // Given: подготавливаем тестовые данные
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("testuser");
        userRequestDto.setPassword("password123");
        userRequestDto.setEmail("testuser@example.com");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedPassword123");
        user.setEmail("testuser@example.com");

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername("testuser");
        userResponseDto.setEmail("testuser@example.com");

        Role defaultRole = new Role();
        defaultRole.setId(1L);  // Устанавливаем значение id
        defaultRole.setTitle("ROLE_USER");

        // Мокируем методы
        when(roleService.getRoleByTitle("ROLE_USER")).thenReturn(defaultRole);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(userResponseDto);

        // When: вызываем метод createUser
        UserResponseDto createdUser = userService.createUser(userRequestDto);

        // Then: проверяем результат
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("testuser@example.com", createdUser.getEmail());

        // Проверяем, что зависимости были вызваны корректное количество раз
        verify(roleService, times(1)).getRoleByTitle("ROLE_USER");
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(any(User.class), eq(UserResponseDto.class));
    }

    @Test
    void UserServiceImp_DeleteUser_success() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void UserServiceImp_DeleteUser_notFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.deleteUser(userId));
        assertEquals("Error deleting user. Couldn't find user with id:" + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).deleteById(userId);
    }

    @Test
    void testGetUsers() {
        Role role = Role.builder().id(1L).title("ROLE_USER").build();
        Set<Role> roles = new HashSet<>(Collections.singletonList(role));

        List<User> mockUsers = List.of(
                User.builder().id(1L).username("user1").email("user1@example.com").password("password123").roles(roles).build(),
                User.builder().id(2L).username("user2").email("user2@example.com").password("password456").roles(roles).build()
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponseDto> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        // Создаем объект User с помощью билдера
        User user = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .password("password123")
                .build();

        // Создаем объект UserResponseDto, который будет возвращаться после маппинга
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username("user1")
                .email("user1@example.com")
                .build();

        // Мокаем вызов метода репозитория
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Мокаем вызов метода маппинга
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        // Выполняем тестируемый метод
        UserResponseDto result = userService.getUserById(1L);

        // Проверяем результат
        assertNotNull(result);  // Убеждаемся, что результат не null
        assertEquals("user1", result.getUsername());

        // Проверяем, что метод findById был вызван один раз с аргументом 1L
        verify(userRepository, times(1)).findById(1L);
        // Проверяем, что метод маппинга был вызван один раз
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }
}
