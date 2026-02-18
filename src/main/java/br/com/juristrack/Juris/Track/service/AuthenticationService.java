package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AuthenticationRequest;
import br.com.juristrack.Juris.Track.dto.response.AuthenticationResponse;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.security.jwt.JwtService;
import br.com.juristrack.Juris.Track.security.user.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;

    public AuthenticationResponse authenticationLocal(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );

        return new AuthenticationResponse(jwtService.generateToken(authentication));
    }

    public String authenticationGoogle(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        UserAuthentication userAuthentication = new UserAuthentication(
                userAccountService.loadOrCreateByEmail(oidcUser.getEmail(), RolesType.ROLE_LAWYER));

        return jwtService.generateToken(new UsernamePasswordAuthenticationToken(userAuthentication, userAuthentication.getAuthorities()));
    }
}
