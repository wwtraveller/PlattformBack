package de.ait.platform.security.service;

import de.ait.platform.role.entity.Role;
import de.ait.platform.role.reposittory.RoleRepository;
import de.ait.platform.security.entity.AuthInfo;
import de.ait.platform.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class TokenService {
    public static final int ACCESS_DAYS = 7;
    public static final int REFRESH_DAYS = 30;
    private SecretKey accessKey;
    private SecretKey refreshKey;
    private final RoleRepository roleRepository;

    public TokenService(@Value("${key.access}") String refreshPhrase,
                        @Value("${key.refresh}") String accessPhrase,
                        @Autowired RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
    }

    private Date getExperationDate(int days) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant experation = currentDate.plusDays(days)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        Date expirationDate = Date.from(experation);
        return expirationDate;
    }

    public String generateAccessToken(User user) {
        Date expirationDate = getExperationDate(ACCESS_DAYS);
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(accessKey)
                .claim("roles", user.getRoles())
                .claim("name", user.getUsername())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date expirationDate = getExperationDate(REFRESH_DAYS);
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }
    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        String username = claims.getSubject();
        List<LinkedHashMap<String,String>> rolesList = (List<LinkedHashMap<String,String>>)claims.get("roles");
        Set<Role> roles = new HashSet<>();
        for (LinkedHashMap<String,String> roleEntry : rolesList) {
            String roleTitle = roleEntry.get("autority");
            Role role = roleRepository.findRoleByTitle(roleTitle);
            if (role != null) {
                roles.add(role);
            }
        }
        return new AuthInfo(username, roles);
    }


}
