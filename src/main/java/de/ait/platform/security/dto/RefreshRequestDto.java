package de.ait.platform.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Schema(description = "DTO for refresh token request")
public class RefreshRequestDto {

    @Schema(description = "Refresh token provided to obtain a new access token", example = "abc123def456", required = true)
    private String refreshToken;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshRequestDto that)) return false;
        return Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(refreshToken);
    }

    @Override
    public String toString() {
        return "RefreshRequestDto " + refreshToken ;
    }

}
