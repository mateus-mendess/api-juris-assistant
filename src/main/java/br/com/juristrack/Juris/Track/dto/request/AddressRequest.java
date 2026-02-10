package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank
        String street,

        @NotBlank
        Integer number,

        @NotBlank
        String neighborhood,

        @NotBlank
        String city,

        @NotBlank
        String state
) {}
