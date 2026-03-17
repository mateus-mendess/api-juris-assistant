package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.model.entity.Address;

import java.util.UUID;

public class AddressSupport {

    public static AddressRequest validRequest() {
        return new AddressRequest("rua X", "20", "Bairro Santo","",  "Unidos", "AL", "57800-000");
    }

    public static Address validEntity() {
        return new Address(UUID.randomUUID(),
                validRequest().street(),
                validRequest().number(),
                validRequest().neighborhood(),
                validRequest().complement(),
                validRequest().city(),
                validRequest().state(),
                validRequest().zipCode());
    }
}
