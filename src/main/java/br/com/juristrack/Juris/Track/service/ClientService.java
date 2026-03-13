package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.request.DocuementRequest;
import br.com.juristrack.Juris.Track.dto.request.PowerOfAttorneyRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ClientService {

    public ClientResponse save(ClientRequest clientRequest) {
        ClientResponse clientResponse = new ClientResponse(UUID.randomUUID());

        return clientResponse;
    }

    public void uploadPowerOfAttorney(UUID id, PowerOfAttorneyRequest powerOfAttorneyRequest, MultipartFile file) {

    }

    public void uploadDocument(UUID id, DocuementRequest docuementRequest, MultipartFile file) {

    }
}
