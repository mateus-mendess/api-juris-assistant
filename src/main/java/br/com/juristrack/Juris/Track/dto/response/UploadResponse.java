package br.com.juristrack.Juris.Track.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UploadResponse(
        @Schema(description = "Unique identifier of the uploaded file", example = "b7f3c9d2-5678-1234-9101-abc...")
        UUID id,

        @Schema(description = "Relative path where the file is stored", example = "/uploads/documents/file.pdf")
        String relativePath
) {}
