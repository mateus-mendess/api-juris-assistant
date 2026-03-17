package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank
        String street,

        @NotBlank
        String number,

        @NotBlank
        String neighborhood,

        String complement,

        @NotBlank
        String city,

        @NotBlank
        String state,

        @NotBlank
        String zipCode
) {}
