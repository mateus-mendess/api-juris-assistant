package br.com.juristrack.Juris.Track.dto.response;

import java.time.Instant;
import java.util.UUID;

public record LawyerResponse(
        UUID id,
        String name,
        String email
) {}
