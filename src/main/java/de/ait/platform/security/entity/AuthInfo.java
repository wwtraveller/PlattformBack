package de.ait.platform.security.entity;

import de.ait.platform.role.entity.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;


@Getter
public class AuthInfo implements Authentication {
    private boolean authenticated;
    private String username;
    private Set<Role> roles;

    public AuthInfo(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthInfo authInfo)) return false;

        return Objects.equals(username, authInfo.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;

    }
}
