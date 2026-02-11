package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.AuthenticationRequest;
import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password());

        AuthenticationResponse authenticationResponse = authenticationService.authentication(authentication);

        return ResponseEntity.ok().body(authenticationResponse);
    }
}
