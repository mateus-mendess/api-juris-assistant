package br.com.juristrack.Juris.Track.security.service;

import br.com.juristrack.Juris.Track.enums.AuthProvider;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.RoleRepository;
import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import br.com.juristrack.Juris.Track.security.user.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2Service implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

        UserAccount user = userAccountRepository.findByEmail(oidcUser.getEmail())
                .orElseGet(() -> createUserGoogle(oidcUser, RolesType.ROLE_LAWYER));

        UserAuthentication userAuthentication = new UserAuthentication(user);

        return new DefaultOidcUser(
                userAuthentication.getAuthorities(),
                oidcUser.getIdToken()
        );
    }

    private UserAccount createUserGoogle(OidcUser oidcUser, RolesType rolesType) {
        Role role = roleRepository.findByName(rolesType.name())
                .orElseThrow(() -> new NotFoundException("Role not found."));

        return userAccountRepository.save(
                UserAccount.builder()
                        .email(oidcUser.getEmail())
                        .authProvider(AuthProvider.GOOGLE)
                        .roles(Set.of(role))
                        .build()
        );
    }
}
