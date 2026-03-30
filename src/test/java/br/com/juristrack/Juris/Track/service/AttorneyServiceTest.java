package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.OabAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.AttorneyMapper;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import br.com.juristrack.Juris.Track.model.entity.Role;
import br.com.juristrack.Juris.Track.model.entity.User;
import br.com.juristrack.Juris.Track.model.repository.AttorneyRepository;
import br.com.juristrack.Juris.Track.support.AddressSupport;
import br.com.juristrack.Juris.Track.support.AttorneySupport;
import br.com.juristrack.Juris.Track.support.UserSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AttorneyServiceTest {
    @Mock
    private AttorneyRepository attorneyRepository;

    @Mock
    private AttorneyMapper attorneyMapper;

    @Mock
    private UserService userService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private AddressService addressService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AttorneyService attorneyService;

    @Captor
    private ArgumentCaptor<Attorney> attorneyArgumentCaptor;

    @Nested
    class FindAllAttorneys {
        @Test
        void should_return_all_lawyer_when_exists_in_db() {
            //Arrange
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            List<Attorney> attorneys = new ArrayList<>();
            attorneys.add(attorney);

            AttorneyResponse attorneyResponse = AttorneySupport.validResponse(attorney);
            List<AttorneyResponse> attorneyResponses = new ArrayList<>();
            attorneyResponses.add(attorneyResponse);

            when(attorneyRepository.findAll()).thenReturn(attorneys);
            when(attorneyMapper.toAttorneysResponse(attorneys)).thenReturn(attorneyResponses);

            //Act & Assert
            var response = assertDoesNotThrow(() -> attorneyService.findAll());

            assertEquals(attorneys.size(), response.size());
        }
    }

    @Nested
    class CreateAttorney {

        @Test
        void should_create_attorney_when_data_is_valid() {
            //Arrange
            AttorneyRequest attorneyRequest = AttorneySupport.validRequest();
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            AttorneyResponse attorneyResponse = AttorneySupport.validResponse(attorney);

            when(attorneyRepository.existsByPhone(attorneyRequest.phone())).thenReturn(false);
            when(attorneyRepository.existsByOabNumberAndOabState(attorneyRequest.oabNumber(), attorneyRequest.oabState())).thenReturn(false);
            when(attorneyRepository.existsByCpf(attorneyRequest.cpf())).thenReturn(false);
            when(userService.create(eq(attorneyRequest.userRequest()), any(AuthProviderType.class), any(RolesType.class))).thenReturn(user);
            when(attorneyMapper.toAttorney(attorneyRequest)).thenReturn(attorney);
            when(addressService.buildAddress(attorneyRequest.addressRequest())).thenReturn(address);
            when(attorneyRepository.save(any(Attorney.class))).thenReturn(attorney);
            when(attorneyMapper.toAttorneyResponse(attorney)).thenReturn(attorneyResponse);

            //Act & Assert
            var response = assertDoesNotThrow(() -> attorneyService.create(attorneyRequest));

            verify(userService).create(attorneyRequest.userRequest(), AuthProviderType.LOCAL, RolesType.ROLE_ATTORNEY);
            verify(attorneyMapper).toAttorney(attorneyRequest);
            verify(attorneyRepository).save(attorneyArgumentCaptor.capture());

            var captered = attorneyArgumentCaptor.getValue();

            assertEquals(attorney.getId(), captered.getId());
            assertEquals(attorneyRequest.cpf(), captered.getCpf());
            assertEquals(captered.getId(), response.id());
        }

        @Test
        void should_throw_CpfAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            AttorneyRequest attorneyRequest = AttorneySupport.validRequest();

            when(attorneyRepository.existsByCpf(attorneyRequest.cpf())).thenReturn(true);

            //Act & Assert
            assertThrows(CpfAlreadyExistsException.class, ()->
                    attorneyService.create(attorneyRequest));

            verify(attorneyRepository, times(0)).save(any(Attorney.class));
        }

        @Test
        void should_throw_OabAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            AttorneyRequest attorneyRequest = AttorneySupport.validRequest();

            when(attorneyRepository.existsByOabNumberAndOabState(attorneyRequest.oabNumber(), attorneyRequest.oabState())).thenReturn(true);

            //Act & Assert
            assertThrows(OabAlreadyExistsException.class, ()->
                    attorneyService.create(attorneyRequest));

            verify(attorneyRepository, times(0)).save(any(Attorney.class));
        }

        @Test
        void should_throw_PhoneAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            AttorneyRequest attorneyRequest = AttorneySupport.validRequest();

            when(attorneyRepository.existsByPhone(attorneyRequest.phone())).thenReturn(true);

            //Act e Assert
            assertThrows(PhoneAlreadyExistsException.class, ()->
                    attorneyService.create(attorneyRequest));

            verify(attorneyRepository, times(0)).save(any(Attorney.class));
        }
    }
}