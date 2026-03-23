package br.com.juristrack.Juris.Track.dto.request;

import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ClientRequest(
        @Schema(description = "Client's full name", example = "Maria Oliveira", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,100}$",
                message = "invalid name.")
        String name,

        @Schema(description = "CPF (Brazilian individual taxpayer registry number)", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @CPF
        String cpf,

        @Schema(description = "Client's nationality", example = "Brazilian", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,100}$",
                message = "invalid nationality.")
        String nationality,

        @Schema(description = "Client's marital status", example = "SINGLE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        MaritalStatusType maritalStatus,

        @Schema(description = "Client's profession or occupation", example = "Software Developer", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String work,

        @Schema(description = "Phone number with area code", example = "(11) 91234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$",
                message = "invalid phone")
        String phone,

        @Schema(description = "Client's address information", requiredMode = Schema.RequiredMode.REQUIRED)
        AddressRequest addressRequest
) {}
