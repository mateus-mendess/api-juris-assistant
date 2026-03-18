package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    class FindByAttorney {

        @Test
        void should_return_attorney_when_exists_in_db() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            AttorneyResponse attorneyResponse = AttorneySupport.validResponse(attorney);
            UUID id = attorney.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyMapper.toAttorneyResponse(attorney)).thenReturn(attorneyResponse);
            when(attorneyRepository.findById(id)).thenReturn(Optional.of(attorney));

            //Act & Assert
           var response = assertDoesNotThrow(() -> attorneyService.findByLawyer(jwt));

            assertEquals(attorney.getId(), response.id());
        }

        @Test
        void should_throw_NotFoundException_when_attorney_not_exists_in_db() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            UUID id = attorney.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () ->
                    attorneyService.findByLawyer(jwt));

            assertEquals("User not found.", exception.getMessage());
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

    @Nested
    class UploadAvatarAttorney {
        @Test
        void should_upload_photo_with_success_when_exists() {
            //Arrange
            MultipartFile photo = mock(MultipartFile.class);
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            Jwt jwt = mock(Jwt.class);
            UUID id = attorney.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.of(attorney));
            when(fileStorageService.save(photo, FileType.AVATAR)).thenReturn("/folder/file");
            when(attorneyRepository.save(attorney)).thenReturn(attorney);

            //Act & Assert
            assertDoesNotThrow(() -> attorneyService.uploadPhoto(jwt, photo));

            verify(attorneyRepository).save(attorneyArgumentCaptor.capture());

            var captured = attorneyArgumentCaptor.getValue();

            assertNotNull(captured.getProfilePhotoPath());
        }
    }

    @Nested
    class UpdateAttorney {
        @Test
        void should_update_data_attorney_with_success() {
            //Arrange
            AttorneyUpdateRequest attorneyUpdateRequest =  AttorneySupport.validUpdateRequest();

            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            Jwt jwt = mock(Jwt.class);
            UUID id = attorney.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.of(attorney));
            when(attorneyRepository.existsByPhone(attorneyUpdateRequest.phone())).thenReturn(false);
            doNothing().when(attorneyMapper).toUpdateAttorney(attorneyUpdateRequest, attorney);

            //Act & Assert
            assertDoesNotThrow(() -> attorneyService.update(attorneyUpdateRequest, jwt));

            verify(attorneyMapper).toUpdateAttorney(eq(attorneyUpdateRequest), attorneyArgumentCaptor.capture());

            var captured = attorneyArgumentCaptor.getValue();

            assertEquals(id, captured.getId());
        }

        @Test
        void should_throw_NotFoundException_when_attorney_not_exists_in_db() {
            //Arrange
            AttorneyUpdateRequest attorneyUpdateRequest =  AttorneySupport.validUpdateRequest();
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () ->
                    attorneyService.update(attorneyUpdateRequest, jwt));

            verify(attorneyMapper, times(0)).toUpdateAttorney(any(AttorneyUpdateRequest.class), any(Attorney.class));
            assertEquals("User not found", exception.getMessage());
        }

        @Test
        void should_throw_PhoneAlreadyExistsException_when_phone_exists_in_db() {
            //Arrange
            AttorneyUpdateRequest attorneyUpdateRequest =  AttorneySupport.validUpdateRequest();
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.of(attorney));
            when(attorneyRepository.existsByPhone(attorneyUpdateRequest.phone())).thenReturn(true);

            //Act & Assert
            var exception = assertThrows(PhoneAlreadyExistsException.class, () ->
                    attorneyService.update(attorneyUpdateRequest, jwt));

            verify(attorneyMapper, times(0)).toUpdateAttorney(any(AttorneyUpdateRequest.class), any(Attorney.class));
            assertEquals("Phone already registered: " + attorneyUpdateRequest.phone(), exception.getMessage());
        }
    }

    @Nested
    class DeleteAttorney {
        @Test
        void should_delete_attorney_with_success() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            User user = UserSupport.validEntity(new Role());
            Address address = AddressSupport.validEntity();
            Attorney attorney = AttorneySupport.validEntity(user, address);
            UUID id = attorney.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.of(attorney));
            doNothing().when(fileStorageService).delete(attorney.getProfilePhotoPath());
            doNothing().when(attorneyRepository).delete(attorney);

            //Act & Assert
            assertDoesNotThrow(() -> attorneyService.delete(jwt));

            verify(attorneyRepository).findById(id);
            verify(attorneyRepository).delete(attorneyArgumentCaptor.capture());

            var captured = attorneyArgumentCaptor.getValue();

            assertEquals(attorney.getId(), captured.getId());
        }

        @Test
        void should_throw_NotFoundException_when_attorney_not_exists_in_db() {
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(attorneyRepository.findById(id)).thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class, () ->
                    attorneyService.delete(jwt));

            verify(attorneyRepository, times(0)).delete(any(Attorney.class));
            assertEquals("User not found.", exception.getMessage());
        }
    }

}