package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record AttorneyRequest(

        @NotBlank
        String name,

        @NotBlank
        @CPF
        String cpf,

        @NotBlank
        String oabNumber,

        @NotBlank
        String oabState,

        @Past(message = "birthday invalid.")
        LocalDate birthday,

        @NotBlank
        @Pattern(regexp = "^(\\(?\\d{2}\\)?\\s?)?9?\\d{4}-?\\d{4}$",
        message = "phone invalid.")
        String phone,

        UserRequest userRequest,

        AddressRequest addressRequest
) {}
