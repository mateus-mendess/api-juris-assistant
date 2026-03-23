package br.com.juristrack.Juris.Track.dto.response;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record ClientResponse(
        @Schema(description = "", example = "")
        UUID id,

        @Schema(description = "Client's full name", example = "Maria Oliveira")
        String name,

        @Schema(description = "CPF (Brazilian individual taxpayer registry number)", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Client's nationality", example = "Brazilian")
        String nationality,

        @Schema(description = "Client's marital status", example = "SINGLE")
        MaritalStatusType maritalStatus,

        @Schema(description = "Client's profession or occupation", example = "Software Developer")
        String work,

        @Schema(description = "Phone number with area code", example = "(11) 91234-5678")
        String phone,

        @Schema(description = "Client's address information")
        AddressResponse addressResponse
) {}
