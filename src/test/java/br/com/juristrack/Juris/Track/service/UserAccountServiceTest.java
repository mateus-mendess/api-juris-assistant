package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.UserAccountRequest;
import br.com.juristrack.Juris.Track.enums.Provider;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.EmailAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.mapper.UserAccountMapper;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.RoleRepository;
import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import br.com.juristrack.Juris.Track.support.UserAccountSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {
    @Mock
    private UserAccountMapper userAccountMapper;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountService userAccountService;

    @Nested
    class CreateUser {

        @Test
        void should_create_user_with_success() {
            //Arrange
            UserAccountRequest request = UserAccountSupport.validRequest();
            Role role = new Role();
            UserAccount entity = UserAccountSupport.validEntity();

            when(userAccountRepository.existsByEmail(request.email())).thenReturn(false);
            when(roleRepository.findByName(RolesType.ROLE_LAWYER.name())).thenReturn(Optional.of(role));
            when(userAccountMapper.toUserAccount(request, Provider.LOCAL)).thenReturn(entity);
            when(passwordEncoder.encode(request.password())).thenReturn("encodePassword");

            //Act & Assert
            var userAccount = assertDoesNotThrow(() -> userAccountService.create(request, Provider.LOCAL, RolesType.ROLE_LAWYER));

            verify(userAccountMapper).toUserAccount(request, Provider.LOCAL);

            assertEquals(request.email(), userAccount.getEmail());
            assertNotNull(userAccount.getRoles());
        }

        @Test
        void should_throw_EmailAlreadyExistsException_when_email_exists_in_db() {
            //Arrange
            UserAccountRequest request = UserAccountSupport.emailInvalid();

            when(userAccountRepository.existsByEmail(request.email())).thenReturn(true);

            //Act e Assert
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userAccountService.create(request, Provider.LOCAL, RolesType.ROLE_LAWYER));

            verify(roleRepository, times(0)).findByName(RolesType.ROLE_LAWYER.name());
            verify(userAccountMapper, times(0)).toUserAccount(request, Provider.LOCAL);
        }

        @Test
        void should_throw_NotFoundException_when_role_not_exists_in_db() {
            //Arrange
            UserAccountRequest request = UserAccountSupport.validRequest();
            RolesType rolesType = RolesType.ROLE_MODERATOR;

            when(roleRepository.findByName(rolesType.name())).thenReturn(Optional.empty());

            //Act e Assert
            var exception = assertThrows(NotFoundException.class,
                    () -> userAccountService.create(request, Provider.LOCAL, rolesType));

            verify(userAccountMapper, times(0)).toUserAccount(any(UserAccountRequest.class), any(Provider.class));

            assertEquals("role not found.", exception.getMessage());
        }
    }

    @Nested
    class CreateOfGoogle {
        @Test
        void should_create_of_google_when_not_exists_in_db() {
            //Arrange
            OidcUser oidcUser = mock(OidcUser.class);
            UserAccount entity = UserAccountSupport.validEntity();
            Role role = new Role();

            //Act & Assert
            when(oidcUser.getEmail()).thenReturn("teste@gmail.com");
            when(roleRepository.findByName(RolesType.ROLE_LAWYER.name())).thenReturn(Optional.of(role));
            when(userAccountRepository.findByEmail(oidcUser.getEmail())).thenReturn(Optional.empty());
            when(userAccountRepository.save(any(UserAccount.class))).thenReturn(entity);

            UserAccount userAccount = assertDoesNotThrow(() -> userAccountService.loadOrCreateByEmail(oidcUser.getEmail(), RolesType.ROLE_LAWYER));

            assertEquals(oidcUser.getEmail(), userAccount.getEmail());
        }

        @Test
        void should_throw_NotFoundException_when_role_not_exists_in_db() {
            //Arrange
            OidcUser oidcUser = mock(OidcUser.class);
            RolesType rolesType = RolesType.ROLE_MODERATOR;

            when(oidcUser.getEmail()).thenReturn("teste@gmail.com");
            when(roleRepository.findByName(rolesType.name())).thenReturn(Optional.empty());

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () ->
                    userAccountService.loadOrCreateByEmail(oidcUser.getEmail(), rolesType));

            assertEquals("role not found.", exception.getMessage());
        }
    }

}