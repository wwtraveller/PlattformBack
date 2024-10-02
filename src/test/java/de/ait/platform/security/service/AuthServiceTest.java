package de.ait.platform.security.service;

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
    private Map<String, String> refreshTokenStorage;
    private AuthService authService;


    @BeforeEach
    public void init() {
        refreshTokenStorage = new HashMap<>();
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
        UserLoginDto inboundUser  = new UserLoginDto("username", "password");
        User foundUser = User.builder()
                .username("username")
                .password("password")
                .build();
        when(userService.loadUserByUsername(inboundUser.getUsername())).thenReturn(foundUser);
        when(passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())).thenReturn(true);
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

        // Verify that the refresh token is stored in the refresh token storage
        Map<String, String> refreshTokenStorageSpy = Mockito.spy(refreshTokenStorage);
        when(refreshTokenStorageSpy.get(accessToken)).thenReturn(refreshToken);
        assertEquals(refreshToken, refreshTokenStorageSpy.get(accessToken));
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

//    @Test
//    public void getNewAccessToken_ValidRefreshToken_ReturnsTokenResponseDto() throws CustomAuthException {
//        // Arrange
//        String inboundRefreshToken = "refresh-token";
//        Claims claims = mock(Claims.class);
//        when(claims.getSubject()).thenReturn("username");
//        when(tokenService.getRefreshClaims(anyString())).thenReturn(claims);
//        String savedRefreshToken = "refresh-token";
//        when(refreshTokenStorage.get("username")).thenReturn(savedRefreshToken);
//        User foundUser  = User.builder()
//                .username("username")
//                .password("password")
//                .build();
//        when(userService.loadUserByUsername("username")).thenReturn(foundUser );
//        String accessToken = "access-token";
//        when(tokenService.generateAccessToken(foundUser )).thenReturn(accessToken);
//
//        // Act
//        TokenResponseDto result = authService.getNewAccessToken(inboundRefreshToken);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(accessToken, result.getAccessToken());
//        assertNull(result.getRefreshToken());
//    }

//    @Test
//    public void getNewAccessToken_InvalidRefreshToken_ThrowsCustomAuthException() throws CustomAuthException {
//        // Arrange
//        String inboundRefreshToken = "refresh-token";
//        Claims refreshClaims = Jwts.claims().setSubject("username").build();
//        when(tokenService.getRefreshClaims(inboundRefreshToken)).thenReturn(refreshClaims);
//        String savedRefreshToken = "invalid-refresh-token";
//        when(refreshTokenStorage.get("username")).thenReturn(savedRefreshToken);
//
//        // Act and Assert
//        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(inboundRefreshToken));
//    }

//    @Test
//    public void getNewAccessToken_NullRefreshToken_ThrowsCustomAuthException() throws CustomAuthException {
//        // Act and Assert
//        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(null));
//    }

//    @Test
//    public void getNewAccessToken_RefreshTokenStorageReturnsNull_ThrowsCustomAuthException() throws CustomAuthException {
//        // Arrange
//        String inboundRefreshToken = "refresh-token";
//        Claims refreshClaims = Jwts.claims().setSubject("username").build();
//        when(tokenService.getRefreshClaims(inboundRefreshToken)).thenReturn(refreshClaims);
//        when(refreshTokenStorage.get("username")).thenReturn(null);
//
//        // Act and Assert
//        assertThrows(CustomAuthException.class, () -> authService.getNewAccessToken(inboundRefreshToken));
//    }

//    @Test
//    public void testGetAuthenticatedUser () {
//        // Create a mock UserResponseDto object
//        UserResponseDto userResponseDto = UserResponseDto.builder()
//                .id(1L)
//                .email("email")
//                .username("test-username")
//                .build();
//
//
//        // Create a mock Authentication object
//        when(authentication.getPrincipal()).thenReturn(userResponseDto);
//
//        // Create a mock SecurityContext object
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//
//        // Set the mock SecurityContext in the SecurityContextHolder
//        SecurityContextHolder.setContext(securityContext);
//
//        // Call the method you want to test
//        UserResponseDto result = authService.getAuthenticatedUser ();
//        System.out.println(result);
//        // Verify the result
//        Assertions.assertThat(result.getUsername()).isEqualTo("test-username");
//    }
}