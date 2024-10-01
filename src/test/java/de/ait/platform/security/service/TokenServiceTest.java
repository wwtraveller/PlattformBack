package de.ait.platform.security.service;

import de.ait.platform.role.entity.Role;
import de.ait.platform.role.reposittory.RoleRepository;
import de.ait.platform.security.entity.AuthInfo;
import de.ait.platform.user.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Value;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("local")
@SpringBootTest
public class TokenServiceTest {

    @Value("${key.access}")
    private String accessPhrase;

    @Value("${key.refresh}")
    private String refreshPhrase;

    @Mock
    private RoleRepository roleRepository;


    private TokenService tokenService;

    @BeforeEach
    public void init(){
        roleRepository = Mockito.mock(RoleRepository.class);
        tokenService = new TokenService(accessPhrase, refreshPhrase, roleRepository);
    }

    @Test
    public void testGenerateAccessToken() {
        // Arrange
        Role role = Role.builder()
                .id(1L)
                .title("ROLE_USER")
                .build();
        User user = User.builder()
                .id(1L)
                .username("Username")
                .email("Email")
                .password("Password")
                .roles(new HashSet<Role>())
                .build();

        user.getRoles().add(role);
        when(roleRepository.findRoleByTitle(any())).thenReturn(role);

        // Act
        String accessToken = tokenService.generateAccessToken(user);

        // Assert
        assertNotNull(accessToken);
        assertTrue(tokenService.validateAccessToken(accessToken));
    }

    @Test
    public void testGenerateRefreshToken() {
        // Arrange
        User user = User.builder()
                .username("Username")
                .email("Email")
                .password("Password")
                .build();


        // Act
        String refreshToken = tokenService.generateRefreshToken(user);

        // Assert
        assertNotNull(refreshToken);
        assertTrue(tokenService.validateRefreshToken(refreshToken));
    }

    @Test
    public void testValidateAccessToken() {
        // Arrange
        Role role = Role.builder()
                .id(1L)
                .title("ROLE_USER")
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .id(1L)
                .username("Username")
                .email("Email")
                .password("Password")
                .roles(roles)
                .build();
        when(roleRepository.findRoleByTitle(any())).thenReturn(role);
        String accessToken = tokenService.generateAccessToken(user);

        // Act and Assert
        assertTrue(tokenService.validateAccessToken(accessToken));
    }

    @Test
    public void testValidateRefreshToken() {
        // Arrange
        User user = User.builder()
                .username("Username")
                .email("Email")
                .password("Password")
                .build();

        String refreshToken = tokenService.generateRefreshToken(user);

        // Act and Assert
        assertTrue(tokenService.validateRefreshToken(refreshToken));
    }

    @Test
    public void testGetAccessClaims() {
        // Arrange
        Role role = Role.builder()
                .id(1L)
                .title("ROLE_USER")
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .id(1L)
                .username("Username")
                .email("Email")
                .password("Password")
                .roles(roles)
                .build();
        user.getRoles().add(role);
        when(roleRepository.findRoleByTitle(any())).thenReturn(role);
        String accessToken = tokenService.generateAccessToken(user);

        // Act
        Claims claims = tokenService.getAccessClaims(accessToken);

        // Assert
        assertNotNull(claims);
        assertEquals(user.getUsername(), claims.getSubject());
    }

    @Test
    public void testGetRefreshClaims() {
        // Arrange
        User user = User.builder()
                .username("Username")
                .email("Email")
                .password("Password")
                .build();

        String refreshToken = tokenService.generateRefreshToken(user);

        // Act
        Claims claims = tokenService.getRefreshClaims(refreshToken);

        // Assert
        assertNotNull(claims);
        assertEquals(user.getUsername(), claims.getSubject());
    }

    @Test
    public void testMapClaimsToAuthInfo() {
        // Arrange
        Role role = Role.builder()
                .id(1L)
                .title("ROLE_USER")
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .id(1L)
                .username("Username")
                .email("Email")
                .password("Password")
                .roles(roles)
                .build();
        user.getRoles().add(role);
        when(roleRepository.findRoleByTitle(any())).thenReturn(role);
        String accessToken = tokenService.generateAccessToken(user);
        Claims claims = tokenService.getAccessClaims(accessToken);

        // Act
        AuthInfo authInfo = tokenService.mapClaimsToAuthInfo(claims);

        // Assert
        assertNotNull(authInfo);
        assertEquals(user.getUsername(), authInfo.getUsername());
        assertEquals(user.getRoles(), authInfo.getRoles());
    }
}