package de.ait.platform.user.controller;

import de.ait.platform.user.dto.RefreshRequestDto;
import de.ait.platform.user.dto.TokenResponseDto;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.service.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

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
}
