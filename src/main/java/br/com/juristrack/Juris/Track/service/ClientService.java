package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.ClientRequest;
import br.com.juristrack.Juris.Track.dto.response.ClientResponse;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.ClientMapper;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import br.com.juristrack.Juris.Track.model.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final LawyerService lawyerService;
    private final AddressService addressService;

    public Client findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found."));
    }

    @Transactional
    public ClientResponse save(ClientRequest request, Jwt jwt) {
        validateRegistrationData(request.cpf(), request.phone());

        Lawyer lawyer = lawyerService.getAuthenticatedLawyer(jwt);
        Address address = addressService.buildAddress(request.addressRequest());

        Client client = clientMapper.toClient(request);

        client.linkAddressAndAttorney(address, lawyer);

        return clientMapper.toClientResponse(clientRepository.save(client));
    }

    private void validateRegistrationData(String cpf, String phone) {
        if (clientRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException(cpf, "CPF");
        }

        if (clientRepository.existsByPhone(phone)) {
            throw new PhoneAlreadyExistsException(phone, "Phone");
        }
    }
}
