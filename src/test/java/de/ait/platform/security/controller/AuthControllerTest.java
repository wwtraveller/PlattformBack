package de.ait.platform.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.security.dto.RefreshRequestDto;
import de.ait.platform.security.dto.TokenResponseDto;
import de.ait.platform.security.exception.CustomAuthException;
import de.ait.platform.security.exception.InvalidPasswordException;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;



    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(authController) // Обработчики исключений
                .build();
    }

    @Test
    void handleInvalidPasswordException() throws Exception {
        // Мокаем поведение, чтобы метод login выбросил InvalidPasswordException
        when(authService.login(any(UserLoginDto.class)))
                .thenThrow(new InvalidPasswordException("Invalid password"));

        // Выполняем запрос и проверяем, что вернется статус 401
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserLoginDto.builder()
                                .username("user")
                                .password("wrong_password")
                                .build())))
                .andExpect(status().isUnauthorized())  // Проверяем, что статус 401
                .andExpect(content().string("Invalid password"));  // Проверяем сообщение об ошибке
    }


    @Test
    void handleAuthException() throws Exception {
        when(authService.login(any(UserLoginDto.class)))
                .thenThrow(new CustomAuthException("Invalid token"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserLoginDto.builder()
                                .username("user")
                                .password("password")
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    void login() throws Exception {
        UserLoginDto loginDto = UserLoginDto.builder()
                .username("user")
                .password("password123")
                .build();

        TokenResponseDto tokenResponse = TokenResponseDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(authService.login(any(UserLoginDto.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void getNewAccessToken() throws Exception {
        // Создаем объект RefreshRequestDto через сеттер
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto();
        refreshRequestDto.setRefreshToken("refreshToken");

        // Мокаем ответ от authService
        TokenResponseDto tokenResponse = TokenResponseDto.builder()
                .accessToken("newAccessToken")
                .refreshToken("refreshToken")
                .build();

        when(authService.getNewAccessToken(any(String.class))).thenReturn(tokenResponse);

        // Выполняем запрос и проверяем статус и значение
        mockMvc.perform(post("/api/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"));
    }

    @Test
    void getAuthenticatedUser() throws Exception {
        UserResponseDto userResponse = UserResponseDto.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .build();

        when(authService.getAuthenticatedUser()).thenReturn(userResponse);

        mockMvc.perform(get("/api/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }
}
