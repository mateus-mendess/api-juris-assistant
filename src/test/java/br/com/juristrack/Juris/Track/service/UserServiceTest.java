package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.UserRequest;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.EmailAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.mapper.UserMapper;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.User;
import br.com.juristrack.Juris.Track.model.repository.RoleRepository;
import br.com.juristrack.Juris.Track.model.repository.UserRepository;
import br.com.juristrack.Juris.Track.support.UserSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUser {

        @Test
        void should_create_user_with_success() {
            //Arrange
            UserRequest request = UserSupport.validRequest();
            Role role = new Role();
            User entity = UserSupport.validEntity(role);

            when(userRepository.existsByEmail(request.email())).thenReturn(false);
            when(roleRepository.findByName(RolesType.ROLE_ATTORNEY.name())).thenReturn(Optional.of(role));
            when(userMapper.toUser(request, AuthProviderType.LOCAL)).thenReturn(entity);
            when(passwordEncoder.encode(request.password())).thenReturn("encodePassword");

            //Act & Assert
            var userAccount = assertDoesNotThrow(() -> userService.create(request, AuthProviderType.LOCAL, RolesType.ROLE_ATTORNEY));

            verify(userMapper).toUser(request, AuthProviderType.LOCAL);

            assertEquals(request.email(), userAccount.getEmail());
            assertNotNull(userAccount.getRoles());
        }

        @Test
        void should_throw_EmailAlreadyExistsException_when_email_exists_in_db() {
            //Arrange
            UserRequest request = UserSupport.emailInvalid();

            when(userRepository.existsByEmail(request.email())).thenReturn(true);

            //Act e Assert
            assertThrows(EmailAlreadyExistsException.class,
                    () -> userService.create(request, AuthProviderType.LOCAL, RolesType.ROLE_ATTORNEY));

            verify(roleRepository, times(0)).findByName(RolesType.ROLE_ATTORNEY.name());
            verify(userMapper, times(0)).toUser(request, AuthProviderType.LOCAL);
        }

        @Test
        void should_throw_NotFoundException_when_role_not_exists_in_db() {
            //Arrange
            UserRequest request = UserSupport.validRequest();
            RolesType rolesType = RolesType.ROLE_MODERATOR;

            when(roleRepository.findByName(rolesType.name())).thenReturn(Optional.empty());

            //Act e Assert
            var exception = assertThrows(NotFoundException.class,
                    () -> userService.create(request, AuthProviderType.LOCAL, rolesType));

            verify(userMapper, times(0)).toUser(any(UserRequest.class), any(AuthProviderType.class));

            assertEquals("role not found.", exception.getMessage());
        }
    }
}