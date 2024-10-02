package de.ait.platform.security.service;

import de.ait.platform.security.entity.AuthInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private final TokenService service;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) request);

        if (token != null && service.validateAccessToken(token)) {
            Claims claims = service.getAccessClaims(token);
            AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);
            authInfo.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authInfo);

        }
        filterChain.doFilter(request, response);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        if (request != null) {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer")) {
                return token.substring(7);
            }
        }
        return null;
    }
}
