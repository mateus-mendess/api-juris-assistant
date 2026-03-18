package br.com.juristrack.Juris.Track.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AttorneyResponse(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {}
