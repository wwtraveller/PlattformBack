package de.ait.platform.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Schema(description = "DTO for token response containing access and refresh tokens")
public class TokenResponseDto {

    @Schema(description = "Access token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh token used to obtain a new access token", example = "abc123def456...")
    private String refreshToken;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenResponseDto that)) return false;

        return Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(refreshToken);
        return result;
    }

    @Override
    public String toString() {
        return "accessToken='" + accessToken + "\' , refreshToken='" + refreshToken + '\'';
    }
}
