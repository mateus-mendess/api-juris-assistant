package br.com.juristrack.Juris.Track.security.jwt;

import br.com.juristrack.Juris.Track.security.user.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant active = Instant.now();
        Long expired = 10800L;

        UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();

        String scopes = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("Juris.exe")
                .issuedAt(active)
                .expiresAt(active.plusSeconds(expired))
                .subject(userAuthentication.getId().toString())
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
