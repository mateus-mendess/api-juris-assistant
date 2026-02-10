package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.UserAccountRequest;
import br.com.juristrack.Juris.Track.enums.Provider;
import br.com.juristrack.Juris.Track.exception.EmailAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.UserAccountMapper;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountMapper userAccountMapper;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public UserAccount create(UserAccountRequest userAccountRequest, Provider provider) {
        validateRegistrationData(userAccountRequest);

        return userAccountMapper.toUserAccount(userAccountRequest, provider);
    }

    private void validateRegistrationData(UserAccountRequest userAccountRequest) {
        if (userAccountRepository.existsByEmail(userAccountRequest.email())) {
            throw new EmailAlreadyExistsException(userAccountRequest.email());
        }
    }
}
