package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.model.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toAddress(AddressRequest request);
}
