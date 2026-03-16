package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
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
import br.com.juristrack.Juris.Track.support.AddressSupport;
import br.com.juristrack.Juris.Track.support.ClientSupport;
import br.com.juristrack.Juris.Track.support.LawyerSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private AddressService addressService;

    @Mock
    private LawyerService lawyerService;

    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;

    @InjectMocks
    private ClientService clientService;

    @Nested
    class FindByClient {
        @Test
        void should_return_client_when_exists() {
            //Arrange
            Client entity = ClientSupport.validEntity();

            when(clientRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

            //Act & Assert
            var result = assertDoesNotThrow(() -> clientService.findById(entity.getId()));

            assertEquals(entity.getId(), result.getId());
        }

        @Test
        void should_throw_NotFoundException_when_client_not_exists() {
            //Arrange
            UUID id = UUID.randomUUID();

            when(clientRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            var result = assertThrows(NotFoundException.class, () -> clientService.findById(id));

            assertEquals("Client not found.", result.getMessage());
        }
    }

    @Nested
    class SaveClient {
        @Test
        void should_save_client_when_data_is_valid() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            ClientRequest clientrequest = ClientSupport.validRequest();
            AddressRequest addressRequest = AddressSupport.validRequest();
            Client clientEntity = ClientSupport.validEntity();
            Address addressEntity = AddressSupport.validEntity();
            Lawyer lawyerEntity = LawyerSupport.validEntity();
            ClientResponse response = ClientSupport.validResponse();

            when(clientRepository.existsByCpf(clientrequest.cpf())).thenReturn(false);
            when(clientRepository.existsByPhone(clientrequest.phone())).thenReturn(false);
            when(lawyerService.getAuthenticatedLawyer(jwt)).thenReturn(lawyerEntity);
            when(addressService.buildAddress(addressRequest)).thenReturn(addressEntity);
            when(clientMapper.toClient(clientrequest)).thenReturn(clientEntity);
            when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
            when(clientMapper.toClientResponse(clientEntity)).thenReturn(response);

            //Act & Assert
            var result = assertDoesNotThrow(() -> clientService.save(clientrequest, jwt));

            verify(lawyerService).getAuthenticatedLawyer(jwt);
            verify(addressService).buildAddress(addressRequest);
            verify(clientRepository).save(clientArgumentCaptor.capture());

            var captured = clientArgumentCaptor.getValue();

            assertEquals(clientrequest.cpf(), captured.getCpf());
            assertEquals(response.id(), result.id());
        }

        @Test
        void should_throw_CpfAlreadyExistsException_when_cpf_already_exists() {
            //Arrange
            ClientRequest request = ClientSupport.validRequest();
            Jwt jwt = mock(Jwt.class);

            when(clientRepository.existsByCpf(request.cpf())).thenReturn(true);

            //Act & Assert
            var result =  assertThrows(CpfAlreadyExistsException.class, () -> clientService.save(request, jwt));

            verify(clientRepository, times(0)).save(any(Client.class));
            assertEquals("CPF already exists in system: " + request.cpf(), result.getMessage());
        }

        @Test
        void should_throw_PhoneAlreadyExistsException_when_cpf_already_exists() {
            //Arrange
            ClientRequest request = ClientSupport.validRequest();
            Jwt jwt = mock(Jwt.class);

            when(clientRepository.existsByPhone(request.phone())).thenReturn(true);

            //Act & Assert
            var result = assertThrows(PhoneAlreadyExistsException.class, () -> clientService.save(request, jwt));

            verify(clientRepository, times(0)).save(any(Client.class));

            assertEquals("Phone already registered: " + request.phone(), result.getMessage());
        }
    }
}