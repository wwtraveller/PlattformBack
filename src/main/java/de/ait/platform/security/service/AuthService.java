package de.ait.platform.security.service;

import de.ait.platform.article.exception.FieldCannotBeNull;
import de.ait.platform.article.exception.FieldIsBlank;
import de.ait.platform.article.exception.FieldIsTaken;
import de.ait.platform.security.dto.TokenResponseDto;
import de.ait.platform.security.exception.InvalidPasswordException;
import de.ait.platform.user.dto.ChangePasswordDto;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.security.exception.CustomAuthException;
import de.ait.platform.user.reposittory.UserRepository;
import de.ait.platform.user.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    final Map<String, String> refreshTokenStorage = new HashMap<>();
    private final UserRepository userRepository;

    public TokenResponseDto login(UserLoginDto inboundUser) throws CustomAuthException {
        if(inboundUser == null || inboundUser.getUsername() == null || inboundUser.getPassword() == null) {
            throw new CustomAuthException("Username or password cannot be null");
        }
        String username = inboundUser.getUsername();
        User foundUser = userService.loadUserByUsername(username);
        if (foundUser == null) {
            log.warn("User not found for username: {}", username);
            throw new CustomAuthException("User not found");
        }


        if (foundUser != null && passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshTokenStorage.put(accessToken, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            log.warn("Invalid password for user: {}", username);
            throw new InvalidPasswordException("Password is not correct");
        }
    }

    public TokenResponseDto getNewAccessToken(String inboundRefreshToken) {
        if (inboundRefreshToken == null) {
            throw new CustomAuthException("Refresh token cannot be null");
        }
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshTokenStorage.get(username);

        if (savedRefreshToken != null && savedRefreshToken.equals(inboundRefreshToken)) {
            User foundUser = userService.loadUserByUsername(username);
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken, null);
        } else {
            throw new CustomAuthException("Invalid refresh token");
        }
    }

    public UserResponseDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.loadUserByUsername(username);
        return mapper.map(user, UserResponseDto.class);
    }

    public  UserResponseDto changePassword(ChangePasswordDto passwordDto){
        UserResponseDto userDto = getAuthenticatedUser();
         User user = userService.loadUserByUsername(userDto.getUsername());
            if (Objects.equals(passwordDto.getNewPassword(), passwordDto.getRepeatPassword())){
                if (passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())){
                    user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
                    userRepository.save(user);
                    return mapper.map(user, UserResponseDto.class);
                }
                else {
                    throw new FieldIsTaken("Wrong password");
                }
            }
            else {
                throw new FieldIsBlank("Password does not match");
            }
        }
}
