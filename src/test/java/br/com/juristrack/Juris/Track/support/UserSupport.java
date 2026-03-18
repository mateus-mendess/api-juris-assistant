package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.UserRequest;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserSupport {

    public static UserRequest validRequest() {
        return new UserRequest("teste@gmail.com", "Teste123@");
    }

    public static User validEntity(Role role) {
        Attorney attorney = new Attorney();

        return new User(
                UUID.randomUUID(),
                attorney,
                "teste@gmail.com",
                AuthProviderType.LOCAL,
                UUID.randomUUID().toString(),
                "Teste123@",
                true,
                Instant.now(),
                new HashSet<>(Set.of(role)));
    }

    public static UserRequest emailInvalid() {
        return new UserRequest("email@email.com", "Teste123@");
    }
}
