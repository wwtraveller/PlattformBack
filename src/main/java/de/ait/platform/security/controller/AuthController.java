package de.ait.platform.security.controller;

import de.ait.platform.security.dto.RefreshRequestDto;
import de.ait.platform.security.dto.TokenResponseDto;
import de.ait.platform.security.exception.CustomAuthException;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.security.service.AuthService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService service;
    private final UserService userService;

    @ExceptionHandler(CustomAuthException.class)
    public void handleAuthException(CustomAuthException e) {
        throw e;
    }

    @Operation(summary = "User login", description = "Allows user to log in and obtain access and refresh tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody UserLoginDto user) {

            return service.login(user);


    }
    @Operation(summary = "Refresh access token", description = "Generates a new access token using the refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New access token generated", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token", content = @Content)
    })

    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto dto) {
        return service.getNewAccessToken(dto.getRefreshToken());
    }
    @Operation(summary = "Get authenticated user", description = "Retrieves the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated user details", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content)
    })
    @GetMapping("/auth/me")
    public UserResponseDto getAuthenticatedUser() {
        return service.getAuthenticatedUser();
    }



}
