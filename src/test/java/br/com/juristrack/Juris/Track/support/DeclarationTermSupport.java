package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.DeclarationTerm;

import java.time.Instant;
import java.util.UUID;

public class DeclarationTermSupport {

    public static DeclarationTerm validEntity(Client client) {
        return new DeclarationTerm(
                UUID.randomUUID(),
                "Declaração",
                "/pasta/arquivo",
                Instant.now(),
                client
        );
    }
}
