package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AuthenticationRequest;
import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import br.com.juristrack.Juris.Track.model.entity.User;
import br.com.juristrack.Juris.Track.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public User getAuthenticatedUser(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        User user = userService.findById(id);

        if (user.getAttorney() == null) {
            throw new IllegalStateException("User without Attorney. Data inconsistency.");
        }

        return user;
    }

    public AuthenticationResponse authenticationLocal(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );

        return new AuthenticationResponse(jwtService.generateToken(authentication));
    }
}
