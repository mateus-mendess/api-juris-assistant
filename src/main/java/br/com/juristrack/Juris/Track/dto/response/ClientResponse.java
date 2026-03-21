package br.com.juristrack.Juris.Track.dto.response;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

public record ClientResponse(
        UUID id,

        String name,

        String cpf,

        String nationality,

        MaritalStatusType maritalStatus,

        String work,

        String phone,

        AddressResponse addressResponse
) {}
