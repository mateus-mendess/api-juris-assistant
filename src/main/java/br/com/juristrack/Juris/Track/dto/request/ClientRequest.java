package br.com.juristrack.Juris.Track.dto.request;

import br.com.juristrack.Juris.Track.enums.ClientStatus;
import br.com.juristrack.Juris.Track.enums.PersonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
        @NotBlank
        String name,

        @NotNull
        PersonType personType,

        @NotBlank
        @Pattern(regexp = "^\\(\\d{2}\\) 9\\d{4}-\\d{4}$",
        message = "phone invalid.")
        String phone,

        @NotNull
        AddressRequest address,

        ClientStatus status
) {}
