package de.ait.platform.user.controller;

import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
//import org.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("user1");
        userRequestDto.setEmail("user1@example.com");
        userRequestDto.setPassword("password123");

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    void testGetUsers() throws Exception {
        List<UserResponseDto> users = Arrays.asList(userResponseDto);

        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("updatedUser");
        userRequestDto.setEmail("updated@example.com");

        UserResponseDto updatedUser = UserResponseDto.builder()
                .id(1L)
                .username("updatedUser")
                .email("updated@example.com")
                .build();

        when(userService.updateUser(any(Long.class), any(UserRequestDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(userResponseDto);

        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    void testCheckUsername() throws Exception {
        when(userService.isUsernameTaken("user1")).thenReturn(true);

        mockMvc.perform(get("/api/check-username")
                        .param("username", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Username is already taken"));
    }

    @Test
    void testCheckEmail() throws Exception {
        when(userService.isEmailTaken("user1@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/check-email")
                        .param("email", "user1@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email is already taken"));
    }
}
