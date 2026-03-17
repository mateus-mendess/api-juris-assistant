package br.com.juristrack.Juris.Track.dto.request;

import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ClientRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,100}$", message = "invalid name.")
        String name,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,100}$", message = "invalid nationality.")
        String nationality,

        @NotNull
        MaritalStatusType maritalStatus,

        @NotBlank
        String work,

        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$", message = "invalid phone")
        String phone,

        AddressRequest addressRequest
) {}
