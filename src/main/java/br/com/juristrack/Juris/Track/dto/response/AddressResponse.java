package br.com.juristrack.Juris.Track.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressResponse(
        @Schema(description = "Street name of the address", example = "Av. Paulista")
        String street,

        @Schema(description = "Street number of the address", example = "1000")
        String number,

        @Schema(description = "Neighborhood or district", example = "Bela Vista")
        String neighborhood,

        @Schema(description = "Additional address information (apartment, block, etc.)", example = "Apt 101")
        String complement,

        @Schema(description = "City where the address is located", example = "São Paulo")
        String city,

        @Schema(description = "State abbreviation (UF)", example = "SP")
        String state,

        @Schema(description = "ZIP code (CEP)", example = "01310-100")
        String zipCode
) {}
