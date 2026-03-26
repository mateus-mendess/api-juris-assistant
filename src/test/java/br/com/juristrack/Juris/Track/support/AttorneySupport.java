package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import br.com.juristrack.Juris.Track.model.entity.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

public class AttorneySupport {

    public static AttorneyRequest validRequest() {
        return new AttorneyRequest(
                "Teste",
                "123.345.789-50",
                "12345",
                "AL",
                LocalDate.parse("1995-06-30"),
                "99450-6070",
                UserSupport.validRequest(),
                AddressSupport.validRequest()
        );
    }

    public static Attorney validEntity(User user, Address address) {
        return new Attorney(
                UUID.randomUUID(),
                user,
                "Teste",
                "123.345.789-50",
                "12345",
                "AL",
                "99450-6070",
                "folder/nomeArquivo.type",
                address,
                new HashSet<>()
        );
    }

    public static AttorneyResponse validResponse(Attorney attorney) {
        return new AttorneyResponse(
                attorney.getId(),
                attorney.getName(),
                attorney.getUser().getEmail(),
                Instant.now()
        );
    }

    public static AttorneyUpdateRequest validUpdateRequest() {
        return new AttorneyUpdateRequest(
                "99240-1010"
        );
    }
}
