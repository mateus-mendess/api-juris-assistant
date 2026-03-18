package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AttorneyUpdateRequest(

        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$",
                message = "phone invalid.")
        String phone
) {}
