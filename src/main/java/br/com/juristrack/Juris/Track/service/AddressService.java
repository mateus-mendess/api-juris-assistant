package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.mapper.AddressMapper;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address save(AddressRequest request) {
        return addressMapper.toAddress(request);
    }
}
