package de.ait.platform.user.controller;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService service;
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })


    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) })
    })
    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return service.getUsers();

    }
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("users/{id}")
    public UserResponseDto getUserById(@PathVariable(name = "id") Long id) {
        return service.getUserById(id);
    }

    @Operation(summary = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return service.updateUser(id, dto);
    }
    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/users/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestBody String username) {
        if (username == null || username.isEmpty()) {
            // Если username пустой, возвращаем статус 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        boolean isAvailable = service.isUsernameAvailable(username);
        return ResponseEntity.ok(isAvailable);

    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        boolean isAvailable = service.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }
}














