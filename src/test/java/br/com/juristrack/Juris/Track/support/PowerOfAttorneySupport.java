package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.PowerOfAttorney;

import java.time.Instant;
import java.util.UUID;

public class PowerOfAttorneySupport {

    public static PowerOfAttorney validEntity(Client client) {
        return new PowerOfAttorney(
                UUID.randomUUID(),
                "Procuração",
                "/pasta/arquivo",
                Instant.now(),
                client
        );
    }
}
