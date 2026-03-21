package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.AuthenticationRequest;
import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Controller responsible for authenticating attorneys in the system.")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate attorney", description = "Authenticate using the email and password that were registered.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attorney successfully authenticated."),
            @ApiResponse(responseCode = "401", description = "The authentication data is incorrect or does not exist in the system.")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {

        AuthenticationResponse authenticationResponse = authenticationService.authenticationLocal(authenticationRequest);

        return ResponseEntity.ok().body(authenticationResponse);
    }
}
