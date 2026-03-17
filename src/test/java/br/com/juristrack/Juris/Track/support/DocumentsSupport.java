package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Document;

import java.time.Instant;
import java.util.UUID;

public class DocumentsSupport {

    public static Document validEntity(Client client) {
        return new Document(
                UUID.randomUUID(),
                "title",
                "/folder/fileName",
                FileType.CONTRACT,
                Instant.now(),
                client
        );
    }
}
