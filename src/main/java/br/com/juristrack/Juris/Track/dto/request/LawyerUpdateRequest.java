package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LawyerUpdateRequest(
        @NotBlank
        @Pattern(regexp = "^[1-9]{2}$",
        message = "DDD invalid.")
        String phoneCode,

        @NotBlank
        @Pattern(regexp = "^9\\d{4}-\\d{4}$",
                message = "phone invalid.")
        String phone
) {}
