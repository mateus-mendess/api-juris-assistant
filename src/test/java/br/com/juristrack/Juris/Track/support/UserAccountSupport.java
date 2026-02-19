package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.UserAccountRequest;
import br.com.juristrack.Juris.Track.enums.AuthProvider;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserAccountSupport {

    public static UserAccountRequest validRequest() {
        return new UserAccountRequest("teste@gmail.com", "Teste123@");
    }

    public static UserAccount validEntity() {
        Lawyer lawyer = new Lawyer();
        Role role = new Role();

        return new UserAccount(
                UUID.randomUUID(),
                lawyer,
                "teste@gmail.com",
                "Teste123@",
                AuthProvider.LOCAL,
                true,
                Instant.now(),
                new HashSet<>(Set.of(role)));
    }

    public static UserAccountRequest emailInvalid() {
        return new UserAccountRequest("email@email.com", "Teste123@");
    }
}
