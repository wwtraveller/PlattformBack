package de.ait.platform.security.service;

import de.ait.platform.security.entity.AuthInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("local")
@SpringBootTest
class TokenFilterTest {
    @Mock
    private TokenService service;

    private TokenFilter filter;

    @BeforeEach
    public void init() {
        service = mock(TokenService.class);
        filter = new TokenFilter(service);
    }

    @Test
    public void doFilter_ValidToken_Authenticated() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "valid-token";
        Claims claims = mock(Claims.class);
        AuthInfo authInfo = mock(AuthInfo.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(service.validateAccessToken(token)).thenReturn(true);
        when(service.getAccessClaims(token)).thenReturn(claims);
        when(service.mapClaimsToAuthInfo(claims)).thenReturn(authInfo);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(service).validateAccessToken(token);
        verify(service).getAccessClaims(token);
        verify(service).mapClaimsToAuthInfo(claims);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilter_InvalidToken_NotAuthenticated() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "invalid-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(service.validateAccessToken(token)).thenReturn(false);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(service).validateAccessToken(token);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilter_NoToken_NotAuthenticated() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void getTokenFromRequest_ValidToken_ReturnsToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Bearer valid-token";

        when(request.getHeader("Authorization")).thenReturn(token);

        // Act
        String result = filter.getTokenFromRequest(request);

        // Assert
        assertEquals("valid-token", result);
    }

    @Test
    public void getTokenFromRequest_InvalidToken_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Invalid token";

        when(request.getHeader("Authorization")).thenReturn(token);

        // Act
        String result = filter.getTokenFromRequest(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTokenFromRequest_NoToken_ReturnsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        String result = filter.getTokenFromRequest(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTokenFromRequest_NullRequest_ReturnsNull() {
        // Act
        String result = filter.getTokenFromRequest(null);

        // Assert
        assertNull(result);
    }
}