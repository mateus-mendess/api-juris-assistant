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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User create(UserRequest userRequest, AuthProviderType authProviderType, RolesType rolesType) {
        validateRegistrationData(userRequest);

        Role role = roleRepository.findByName(rolesType.name())
                .orElseThrow(() -> new NotFoundException("role not found."));

        User user = userMapper.toUser(userRequest, authProviderType);

        if (authProviderType == AuthProviderType.LOCAL) {
            user.setProviderUserId(user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.getRoles().add(role);

        return user;
    }

    private void validateRegistrationData(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new EmailAlreadyExistsException(userRequest.email(), "email");
        }
    }
}
