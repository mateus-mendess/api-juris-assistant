package br.com.juristrack.Juris.Track.dto.request;

import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ClientRequest(
        @NotBlank
        @Pattern(regexp = "", message = "")
        String name,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        @Pattern(regexp = "", message = "")
        String nationality,

        @NotNull
        @Pattern(regexp = "", message = "")
        MaritalStatusType maritalStatus,

        @NotBlank
        String work,

        @NotBlank
        @Pattern(regexp = "", message = "")
        String phone,

        AddressRequest addressRequest
) {}
