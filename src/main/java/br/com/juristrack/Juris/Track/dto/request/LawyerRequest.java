package br.com.juristrack.Juris.Track.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record LawyerRequest(

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
        @Pattern(regexp = "^[1-9]{2}$",
                message = "DDD invalid")
        String phoneCode,

        @NotBlank
        @Pattern(regexp = "^9\\d{4}-\\d{4}$",
        message = "phone invalid.")
        String phone,

        UserAccountRequest userAccountRequest
) {}
