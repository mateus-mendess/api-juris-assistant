package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank
        @Email(message = "email invalid.")
        String email,

        @NotBlank
        String password
) {}
