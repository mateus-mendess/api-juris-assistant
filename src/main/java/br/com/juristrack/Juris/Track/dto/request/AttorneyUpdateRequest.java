package br.com.juristrack.Juris.Track.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AttorneyUpdateRequest(

        @Schema(description = "Updated phone number with area code", example = "(11) 99876-5432")
        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$",
                message = "phone invalid.")
        String phone
) {}
