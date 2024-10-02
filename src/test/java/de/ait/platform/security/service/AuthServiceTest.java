package de.ait.platform.security.service;

import de.ait.platform.security.dto.RefreshRequestDto;
import de.ait.platform.security.dto.TokenResponseDto;
import de.ait.platform.security.exception.CustomAuthException;
import de.ait.platform.security.exception.InvalidPasswordException;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@SpringBootTest
class AuthServiceTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @Mock
    private Authentication authentication;

    private AuthService authService;
    private RefreshRequestDto refreshRequest;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        authentication = Mockito.mock(Authentication.class);
        userService = Mockito.mock(UserService.class);
        tokenService = Mockito.mock(TokenService.class);
        mapper = Mockito.mock(ModelMapper.class);
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        authService = new AuthService(tokenService, userService, passwordEncoder, mapper);
        when(mapper.map(any(UserResponseDto.class), any())).thenReturn(new User());
        when(mapper.map(any(UserRequestDto.class), any())).thenReturn(new User());
        when(mapper.map(any(User.class), any())).thenReturn(new UserResponseDto());

    }

    @Test
    public void login_ValidUser_ReturnsTokenResponseDto() throws CustomAuthException {
        // Arrange
        UserLoginDto inboundUser = new UserLoginDto("username", "password");

        // Mock the user that would be found by the user service
        User foundUser = User.builder()
                .username("username")
                .password("password")
                .build();

        when(userService.loadUserByUsername(inboundUser.getUsername())).thenReturn(foundUser);

        // Mock password matching logic
        when(passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())).thenReturn(true);

        // Mock token generation
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        when(tokenService.generateAccessToken(foundUser)).thenReturn(accessToken);
        when(tokenService.generateRefreshToken(foundUser)).thenReturn(refreshToken);

        // Act
        TokenResponseDto result = authService.login(inboundUser);

        // Assert
        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());

        // Verify that the refresh token is stored
        verify(tokenService, times(1)).generateRefreshToken(foundUser);
    }

    @Test
    public void login_InvalidUser_ThrowsCustomAuthException() throws CustomAuthException {
        // Arrange
        UserLoginDto inboundUser = new UserLoginDto("username", "password");
        when(userService.loadUserByUsername(inboundUser.getUsername())).thenReturn(null);

        // Act and Assert
        assertThrows(CustomAuthException.class, () -> authService.login(inboundUser));
    }

    @Test
    public void login_NullInboundUser_ThrowsCustomAuthException() throws CustomAuthException {
        // Act and Assert
        assertThrows(CustomAuthException.class, () -> authService.login(null));
    }

    @Test
    public void login_NullUsername_ThrowsCustomAuthException() throws CustomAuthException {
        // Arrange
        UserLoginDto inboundUser = new UserLoginDto(null, "password");

        // Act and Assert
        assertThrows(CustomAuthException.class, () -> authService.login(inboundUser));
    }

    @Test
    public void login_NullPassword_ThrowsCustomAuthException() throws CustomAuthException {
        // Arrange
        UserLoginDto inboundUser = new UserLoginDto("username", null);

        // Act and Assert
        assertThrows(CustomAuthException.class, () -> authService.login(inboundUser));
    }

    @Test
    public void login_InvalidPassword_ThrowsInvalidPasswordException() throws CustomAuthException {
        // Arrange
        UserLoginDto inboundUser = new UserLoginDto("username", "password");
        User foundUser = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .build();
        when(userService.loadUserByUsername(inboundUser.getUsername())).thenReturn(foundUser);
        when(passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(InvalidPasswordException.class, () -> authService.login(inboundUser));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
    }

    @Test
    void getNewAccessToken() {
    }

    @Test
    void getAuthenticatedUser() {
    }

    @Test
    public void getNewAccessToken_ValidRefreshToken_ReturnsTokenResponseDto() throws CustomAuthException {
        // Arrange
        String inboundRefreshToken = "valid-refresh-token";

        // Создаем реальные Claims и устанавливаем subject (имя пользователя)
        Claims claims = Jwts.claims().setSubject("username").build();
        when(tokenService.getRefreshClaims(inboundRefreshToken)).thenReturn(claims);

        // Добавляем токен в хранилище refresh токенов для пользователя
        String savedRefreshToken = "valid-refresh-token";
        authService.refreshTokenStorage.put("username", savedRefreshToken);  // Доступ напрямую

        // Имитация загрузки пользователя через userService
        User foundUser = User.builder()
                .username("username")
                .password("password")
                .build();
        when(userService.loadUserByUsername("username")).thenReturn(foundUser);

        // Генерация access токена
        String accessToken = "new-access-token";
        when(tokenService.generateAccessToken(foundUser)).thenReturn(accessToken);

        // Act
        TokenResponseDto result = authService.getNewAccessToken(inboundRefreshToken);

        // Assert
        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());
        assertNull(result.getRefreshToken());  // Проверяем, что refresh токен не генерируется заново
    }

    @Test
    public void getNewAccessToken_InvalidRefreshToken_ThrowsCustomAuthException() {
        // Arrange
        String inboundRefreshToken = "invalid-refresh-token";

        // Создаем реальные Claims и устанавливаем subject (имя пользователя)
        Claims claims = Jwts.claims().setSubject("username").build();
        when(tokenService.getRefreshClaims(inboundRefreshToken)).thenReturn(claims);

        // Добавляем другой токен в хранилище refresh токенов для пользователя
        String savedRefreshToken = "valid-refresh-token";
        authService.refreshTokenStorage.put("username", savedRefreshToken);  // Доступ напрямую

        // Act & Assert
        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(inboundRefreshToken));
    }


    @Test
    public void getNewAccessToken_NullRefreshToken_ThrowsCustomAuthException() {
        // Act & Assert: проверка выброса CustomAuthException при null-токене
        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(null));
    }

    @Test
    public void getNewAccessToken_RefreshTokenStorageReturnsNull_ThrowsCustomAuthException() {
        // Arrange
        String inboundRefreshToken = "refresh-token";

        // Создаем реальные Claims и устанавливаем subject (имя пользователя)
        Claims claims = Jwts.claims().setSubject("username").build();
        when(tokenService.getRefreshClaims(inboundRefreshToken)).thenReturn(claims);

        // Здесь не добавляем ничего в refreshTokenStorage, чтобы он возвращал null для пользователя
        authService.refreshTokenStorage.remove("username"); // Убеждаемся, что хранилище пустое

        // Act & Assert
        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(inboundRefreshToken));
    }

    @Test
    public void getAuthenticatedUser_ReturnsUserResponseDto() {
        // Arrange
        String username = "test-username";

        // Мокируем пользователя, который должен вернуться через userService
        User foundUser = User.builder()
                .username(username)
                .password("password")
                .build();

        // Мокируем UserResponseDto, который будет возвращен после маппинга
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username(username)
                .email("test@example.com")
                .build();

        // Мокируем Authentication, чтобы возвращать имя пользователя
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(username);

        // Мокируем SecurityContext и задаем ему аутентификацию
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокируем загрузку пользователя через userService
        when(userService.loadUserByUsername(username)).thenReturn(foundUser);

        // Мокируем маппинг с помощью ModelMapper
        when(mapper.map(foundUser, UserResponseDto.class)).thenReturn(userResponseDto);

        // Act: вызываем метод для получения аутентифицированного пользователя
        UserResponseDto result = authService.getAuthenticatedUser();

        // Assert: проверяем, что полученный результат содержит ожидаемые значения
        assertNotNull(result);
        assertEquals(username, result.getUsername());  // Проверяем, что username соответствует "test-username"
        assertEquals("test@example.com", result.getEmail());  // Проверяем email
    }
}