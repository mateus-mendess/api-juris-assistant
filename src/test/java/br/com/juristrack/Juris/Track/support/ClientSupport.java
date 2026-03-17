package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Document;

import java.util.HashSet;
import java.util.UUID;

public class ClientSupport {

    public static ClientRequest validRequest() {
        return new ClientRequest("Mateus", "122.637.266-45", "brasileiro", MaritalStatusType.MARRIED, "desenvolvedor", "(82) 99340-0040", AddressSupport.validRequest());
    }

    public static Client validEntity() {

        Client client = new Client(
                UUID.randomUUID(),
                validRequest().name(),
                validRequest().cpf(),
                validRequest().nationality(),
                validRequest().maritalStatus(),
                validRequest().work(),
                validRequest().phone(),
                true,
                LawyerSupport.validEntity(),
                AddressSupport.validEntity(),
                new HashSet<>()
        );

        Document documents = DocumentsSupport.validEntity(client);

        client.getDocuments().add(documents);

        return client;
    }

    public static ClientResponse validResponse() {
        return new ClientResponse(
                validEntity().getId()
        );
    }
}
