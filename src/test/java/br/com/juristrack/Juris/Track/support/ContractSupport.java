package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Contract;

import java.time.Instant;
import java.util.UUID;

public class ContractSupport {

    public static Contract validEntity(Client client) {
        return new Contract(
                UUID.randomUUID(),
                "Contrato",
                "/pasta/arquivo",
                Instant.now(),
                client
        );
    }
}
