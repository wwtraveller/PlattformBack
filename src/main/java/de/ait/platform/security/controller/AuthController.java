package de.ait.platform.security.controller;

import de.ait.platform.security.dto.RefreshRequestDto;
import de.ait.platform.security.dto.TokenResponseDto;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService service;
    private final UserService userService;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody UserLoginDto user) {
        try {
            return service.login(user);
        } catch (AuthException e) {
            return new TokenResponseDto(null, null);
        }
    }

    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto dto) {
        return service.getNewAccessToken(dto.getRefreshToken());
    }

    @GetMapping("/auth/me")
    public UserResponseDto getAuthenticatedUser() {
        return service.getAuthenticatedUser();
    }

}
