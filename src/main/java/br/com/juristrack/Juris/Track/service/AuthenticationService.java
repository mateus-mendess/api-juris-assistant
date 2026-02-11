package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtService jwtService;

    public AuthenticationResponse authentication(Authentication authentication) {
        return new AuthenticationResponse(jwtService.generateToken(authentication));
    }
}
