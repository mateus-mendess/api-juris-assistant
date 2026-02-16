package br.com.juristrack.Juris.Track.handler;

import br.com.juristrack.Juris.Track.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt = authenticationService.authenticationGoogle(authentication);

        response.setContentType("application/json");
        response.getWriter().write("""
                {
                    "token": "%s"
                }
                """.formatted(jwt)
        );


    }
}
