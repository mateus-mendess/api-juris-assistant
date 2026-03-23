package br.com.juristrack.Juris.Track.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @Schema(description = "User email address", example = "user@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Email(message = "email invalid")
        String email,

        @Schema(description = "User password (must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters)",
                example = "P@ssw0rd123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
                message = "The password must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.")
        String password
) {}
