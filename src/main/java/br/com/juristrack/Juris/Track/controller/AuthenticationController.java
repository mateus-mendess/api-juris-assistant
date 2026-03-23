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
@Tag(name = "Authentication", description = "Operations related to authentication and access to the system.")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate attorney", description = "Authenticates an attorney using registered email and password, returning an access token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        return ResponseEntity.ok().body(authenticationResponse);
    }
}
