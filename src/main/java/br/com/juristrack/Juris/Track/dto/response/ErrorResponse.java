package br.com.juristrack.Juris.Track.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime dateTime,
        Integer status,
        String message,
        String field,
        String details
) {}
