package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.request.DocuementRequest;
import br.com.juristrack.Juris.Track.dto.request.PowerOfAttorneyRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import br.com.juristrack.Juris.Track.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid ClientRequest clientRequest) {
        ClientResponse clientResponse = clientService.save(clientRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientResponse.id()).toUri();

        return ResponseEntity.created(uri).body(clientResponse);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> uploadPowerOfAttorney(@PathVariable UUID id,
                                                      @RequestParam(name = "powerOfAttorney")PowerOfAttorneyRequest powerOfAttorneyRequest,
                                                      @RequestParam(name = "file") MultipartFile file) {

        clientService.uploadPowerOfAttorney(id, powerOfAttorneyRequest, file);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/document")
    public ResponseEntity<Void> uploadDocument(@PathVariable UUID id,
                                               @RequestParam(name = "documentRequest") DocuementRequest docuementRequest,
                                               @RequestParam(name = "file") MultipartFile file) {

        clientService.uploadDocument(id, docuementRequest, file);

        return ResponseEntity.noContent().build();
    }
}
