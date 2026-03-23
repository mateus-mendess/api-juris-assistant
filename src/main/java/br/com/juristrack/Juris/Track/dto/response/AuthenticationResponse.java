package br.com.juristrack.Juris.Track.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponse(
        @Schema(description = "JWT token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {}
