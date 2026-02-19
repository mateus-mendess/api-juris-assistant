package br.com.juristrack.Juris.Track.security.service;

import br.com.juristrack.Juris.Track.model.repository.UserAccountRepository;
import br.com.juristrack.Juris.Track.security.user.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(email)
                .map(UserAuthentication::new )
                .orElseThrow(() -> new UsernameNotFoundException("user not found with email: " + email));
    }
}
