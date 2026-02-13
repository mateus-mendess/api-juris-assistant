package br.com.juristrack.Juris.Track.security.user;

import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


@RequiredArgsConstructor
public class UserAuthentication implements UserDetails {

    private final UserAccount userAccount;

    public UUID getId() {
        return userAccount.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAccount.getRoles()
                .stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return userAccount.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userAccount.getIsEnable();
    }
}
