package br.com.juristrack.Juris.Track.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @Schema(description = "Date and time when the error occurred (ISO 8601 format)", example = "2026-03-23T10:15:30")
        LocalDateTime dateTime,

        @Schema(description = "HTTP status code of the error", example = "400")
        Integer status,

        @Schema(description = "General error message describing what went wrong", example = "Validation error")
        String message,

        @Schema(description = "Field that caused the error (when applicable)", example = "email")
        String field,

        @Schema(description = "Detailed explanation of the error", example = "Email must be a valid format")
        String details
) {}
