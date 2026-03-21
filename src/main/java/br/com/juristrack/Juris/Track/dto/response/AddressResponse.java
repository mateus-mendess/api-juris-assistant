package br.com.juristrack.Juris.Track.dto.response;

import jakarta.validation.constraints.NotBlank;

public record AddressResponse(
        String street,

        String number,

        String neighborhood,

        String complement,

        String city,

        String state,

        String zipCode
) {}
