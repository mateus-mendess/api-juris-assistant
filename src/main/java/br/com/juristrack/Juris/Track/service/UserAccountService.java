package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.UserAccountRequest;
import br.com.juristrack.Juris.Track.enums.AuthProvider;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.EmailAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.mapper.UserAccountMapper;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.RoleRepository;
import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountMapper userAccountMapper;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount create(UserAccountRequest userAccountRequest, AuthProvider authProvider, RolesType rolesType) {
        validateRegistrationData(userAccountRequest);

        Role role = roleRepository.findByName(rolesType.name())
                .orElseThrow(() -> new NotFoundException("role not found."));

        UserAccount userAccount = userAccountMapper.toUserAccount(userAccountRequest, authProvider);

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount.getRoles().add(role);

        return userAccount;
    }

    private void validateRegistrationData(UserAccountRequest userAccountRequest) {
        if (userAccountRepository.existsByEmail(userAccountRequest.email())) {
            throw new EmailAlreadyExistsException(userAccountRequest.email(), "email");
        }
    }
}
