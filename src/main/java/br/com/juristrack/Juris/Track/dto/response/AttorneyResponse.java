package br.com.juristrack.Juris.Track.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record AttorneyResponse(
        @Schema(description = "Unique identifier of the attorney", example = "a3f1c9d2-1234-5678-9101-abc...")
        UUID id,

        @Schema(description = "Full name of the attorney", example = "João Silva")
        String name,

        @Schema(description = "Email address of the attorney", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Date and time when the attorney was created (ISO 8601 format)", example = "2026-03-23T10:15:30Z")
        Instant createdAt
) {}
