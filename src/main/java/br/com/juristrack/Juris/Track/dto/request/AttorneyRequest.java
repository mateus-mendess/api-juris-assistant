package br.com.juristrack.Juris.Track.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record AttorneyRequest(

        @Schema(description = "Attorney's full name", example = "João da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String name,

        @Schema(description = "CPF (Brazilian individual taxpayer registry number)", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @CPF
        String cpf,

        @Schema(description = "Attorney's OAB registration number", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String oabNumber,

        @Schema(description = "OAB issuing state (UF)", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String oabState,

        @Schema(description = "Attorney's birth date", example = "1990-05-20", requiredMode = Schema.RequiredMode.REQUIRED)
        @Past(message = "birthday invalid.")
        LocalDate birthday,

        @Schema(description = "Phone number with area code", example = "(11) 91234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$",
        message = "phone invalid.")
        String phone,

        @Schema(description = "User account data associated with the attorney", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequest userRequest,

        @Schema(description = "Attorney's address information", requiredMode = Schema.RequiredMode.REQUIRED)
        AddressRequest addressRequest
) {}
