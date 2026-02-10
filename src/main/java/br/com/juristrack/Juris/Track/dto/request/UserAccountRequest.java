package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAccountRequest(
        @NotBlank
        @Email(message = "email invalid")
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
                message = "The password must contain at least 8 characters, including uppercase, lowercase, numbers, and special characters.")
        String password
) {}
