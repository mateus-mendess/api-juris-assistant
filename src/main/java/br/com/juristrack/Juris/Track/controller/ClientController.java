package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import br.com.juristrack.Juris.Track.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Controller responsible for carrying out actions involving the attorneys' clients.")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Register clients",
            description = """
                Register the customer in the system when the data is valid and the CPF 
                (Brazilian tax identification number) has never been registered.
                """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid ClientRequest request, @AuthenticationPrincipal Jwt jwt) {
        ClientResponse response = clientService.save(request, jwt);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
