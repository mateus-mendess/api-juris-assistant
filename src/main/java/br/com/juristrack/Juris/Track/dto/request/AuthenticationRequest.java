package br.com.juristrack.Juris.Track.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @Schema(description = "User email address used for authentication", example = "user@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email(message = "email invalid.")
        String email,

        @Schema(description = "User password", example = "P@ssw0rd123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String password
) {}
