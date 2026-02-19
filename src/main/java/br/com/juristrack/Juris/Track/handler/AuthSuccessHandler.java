package br.com.juristrack.Juris.Track.handler;

import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import br.com.juristrack.Juris.Track.security.jwt.JwtService;
import br.com.juristrack.Juris.Track.security.user.UserAuthentication;
import br.com.juristrack.Juris.Track.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        UserAccount user = userAccountRepository.findByEmail(oidcUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        UserAuthentication userAuth = new UserAuthentication(user);

        String jwt = jwtService.generateToken(new UsernamePasswordAuthenticationToken(userAuth, userAuth.getAuthorities()));

        response.setContentType("application/json");
        response.getWriter().write("""
                {
                    "token": "%s"
                }
                """.formatted(jwt)
        );
    }
}
