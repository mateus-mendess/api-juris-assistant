package br.com.juristrack.Juris.Track.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @Schema(description = "Street name of the address", example = "Av. Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String street,

        @Schema(description = "Street number of the address", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String number,

        @Schema(description = "Neighborhood or district", example = "Bela Vista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String neighborhood,

        @Schema(description = "Additional address information (apartment, block, etc.)", example = "Apt 101", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String complement,

        @Schema(description = "City where the address is located", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String city,

        @Schema(description = "State abbreviation (UF)", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String state,

        @Schema(description = "ZIP code (CEP)", example = "01310-100", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String zipCode
) {}
