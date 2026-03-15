package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import br.com.juristrack.Juris.Track.model.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toClient (ClientRequest request);

    ClientResponse toClientResponse (Client client);
}
