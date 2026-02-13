package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AuthenticationRequest;
import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );

        return new AuthenticationResponse(jwtService.generateToken(authentication));
    }
}
