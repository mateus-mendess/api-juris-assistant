package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.exception.OabAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.LawyerMapper;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.LawyerRepository;
import br.com.juristrack.Juris.Track.support.LawyerSupport;
import br.com.juristrack.Juris.Track.support.UserAccountSupport;
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
class LawyerServiceTest {
    @Mock
    private LawyerRepository lawyerRepository;

    @Mock
    private LawyerMapper lawyerMapper;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private LawyerService lawyerService;

    @Captor
    private ArgumentCaptor<Lawyer> lawyerArgumentCaptor;

    @Nested
    class findAllLawyers {
        @Test
        void should_return_all_lawyer_when_exists_in_db() {
            //Arrange
            Lawyer lawyer = LawyerSupport.validEntity();
            List<Lawyer> lawyers = new ArrayList<>();
            lawyers.add(lawyer);

            LawyerResponse lawyerResponse = LawyerSupport.validResponse(lawyer);
            List<LawyerResponse> lawyerResponses = new ArrayList<>();
            lawyerResponses.add(lawyerResponse);

            when(lawyerRepository.findAll()).thenReturn(lawyers);
            when(lawyerMapper.toLawyersResponse(lawyers)).thenReturn(lawyerResponses);

            //Act & Assert
            var response = assertDoesNotThrow(() -> lawyerService.findAll());

            assertEquals(lawyers.size(), response.size());
        }
    }

    @Nested
    class findByLawyer {

        @Test
        void should_return_lawyer_when_exists_in_db() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            Lawyer lawyer = LawyerSupport.validEntity();
            LawyerResponse lawyerResponse = LawyerSupport.validResponse(lawyer);
            UUID id = lawyer.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerMapper.toLawyerResponse(lawyer)).thenReturn(lawyerResponse);
            when(lawyerRepository.findById(id)).thenReturn(Optional.of(lawyer));

            //Act & Assert
           var response = assertDoesNotThrow(() -> lawyerService.findById(jwt));

            assertEquals(lawyer.getId(), response.id());
        }

        @Test
        void should_throw_NotFoundException_when_lawyer_not_exists_in_db() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            Lawyer lawyer = LawyerSupport.validEntity();
            UUID id = lawyer.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () ->
                    lawyerService.findById(jwt));

            assertEquals("User not found.", exception.getMessage());
        }
    }

    @Nested
    class CreateLawyer {

        @Test
        void should_create_lawyer_when_data_is_valid() {
            //Arrange
            LawyerRequest lawyerRequest = LawyerSupport.validRequest();
            UserAccount userAccount = UserAccountSupport.validEntity();
            Lawyer lawyer = LawyerSupport.validEntity();
            LawyerResponse lawyerResponse = LawyerSupport.validResponse(lawyer);

            when(lawyerRepository.existsByPhone(lawyerRequest.phone())).thenReturn(false);
            when(lawyerRepository.existsByOabNumberAndOabState(lawyerRequest.oabNumber(), lawyerRequest.oabState())).thenReturn(false);
            when(lawyerRepository.existsByCpf(lawyerRequest.cpf())).thenReturn(false);
            when(userAccountService.create(eq(lawyerRequest.userAccountRequest()), any(AuthProviderType.class), any(RolesType.class))).thenReturn(userAccount);
            when(lawyerMapper.toLawyer(lawyerRequest)).thenReturn(lawyer);
            when(lawyerRepository.save(any(Lawyer.class))).thenReturn(lawyer);
            when(lawyerMapper.toLawyerResponse(lawyer)).thenReturn(lawyerResponse);

            //Act & Assert
            var response = assertDoesNotThrow(() -> lawyerService.create(lawyerRequest));

            verify(userAccountService).create(lawyerRequest.userAccountRequest(), AuthProviderType.LOCAL, RolesType.ROLE_LAWYER);
            verify(lawyerMapper).toLawyer(lawyerRequest);
            verify(lawyerRepository).save(lawyerArgumentCaptor.capture());

            var captered = lawyerArgumentCaptor.getValue();

            assertEquals(lawyer.getId(), captered.getId());
            assertEquals(lawyerRequest.cpf(), captered.getCpf());
            assertEquals(captered.getId(), response.id());
        }

        @Test
        void should_throw_CpfAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            LawyerRequest lawyerRequest = LawyerSupport.validRequest();

            when(lawyerRepository.existsByCpf(lawyerRequest.cpf())).thenReturn(true);

            //Act & Assert
            assertThrows(CpfAlreadyExistsException.class, ()->
                    lawyerService.create(lawyerRequest));

            verify(lawyerRepository, times(0)).save(any(Lawyer.class));
        }

        @Test
        void should_throw_OabAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            LawyerRequest lawyerRequest = LawyerSupport.validRequest();

            when(lawyerRepository.existsByOabNumberAndOabState(lawyerRequest.oabNumber(), lawyerRequest.oabState())).thenReturn(true);

            //Act & Assert
            assertThrows(OabAlreadyExistsException.class, ()->
                    lawyerService.create(lawyerRequest));

            verify(lawyerRepository, times(0)).save(any(Lawyer.class));
        }

        @Test
        void should_throw_PhoneAlreadyExistsException_when_cpf_exists_in_db() {
            //Arrange
            LawyerRequest lawyerRequest = LawyerSupport.validRequest();

            when(lawyerRepository.existsByPhone(lawyerRequest.phone())).thenReturn(true);

            //Act e Assert
            assertThrows(PhoneAlreadyExistsException.class, ()->
                    lawyerService.create(lawyerRequest));

            verify(lawyerRepository, times(0)).save(any(Lawyer.class));
        }
    }

    @Nested
    class UploadAvatarLawyer {
        @Test
        void should_upload_photo_with_success_when_exists() {
            //Arrange
            MultipartFile photo = mock(MultipartFile.class);
            Lawyer lawyer = LawyerSupport.validEntity();
            UUID id = lawyer.getId();

            when(lawyerRepository.findById(id)).thenReturn(Optional.of(lawyer));
            when(fileStorageService.save(photo, FileType.AVATAR)).thenReturn("/folder/file");
            when(lawyerRepository.save(lawyer)).thenReturn(lawyer);

            //Act & Assert
            assertDoesNotThrow(() -> lawyerService.uploadPhoto(id, photo));

            verify(lawyerRepository).save(lawyerArgumentCaptor.capture());

            var captured = lawyerArgumentCaptor.getValue();

            assertNotNull(captured.getProfilePhotoPath());
        }
    }

    @Nested
    class UpdateLawyer {
        @Test
        void should_update_data_lawyer_with_success() {
            //Arrange
            LawyerUpdateRequest lawyerUpdateRequest =  LawyerSupport.validUpdateRequest();

            Lawyer lawyer = LawyerSupport.validEntity();
            Jwt jwt = mock(Jwt.class);
            UUID id = lawyer.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.of(lawyer));
            when(lawyerRepository.existsByPhone(lawyerUpdateRequest.phone())).thenReturn(false);
            doNothing().when(lawyerMapper).toUpdateLawyer(lawyerUpdateRequest, lawyer);

            //Act & Assert
            assertDoesNotThrow(() -> lawyerService.update(lawyerUpdateRequest, jwt));

            verify(lawyerMapper).toUpdateLawyer(eq(lawyerUpdateRequest), lawyerArgumentCaptor.capture());

            var captured = lawyerArgumentCaptor.getValue();

            assertEquals(id, captured.getId());
        }

        @Test
        void should_throw_NotFoundException_when_lawyer_not_exists_in_db() {
            //Arrange
            LawyerUpdateRequest lawyerUpdateRequest =  LawyerSupport.validUpdateRequest();
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.empty());

            //Act & Assert
            var exception = assertThrows(NotFoundException.class, () ->
                    lawyerService.update(lawyerUpdateRequest, jwt));

            verify(lawyerMapper, times(0)).toUpdateLawyer(any(LawyerUpdateRequest.class), any(Lawyer.class));
            assertEquals("User not found", exception.getMessage());
        }

        @Test
        void should_throw_PhoneAlreadyExistsException_when_phone_exists_in_db() {
            //Arrange
            LawyerUpdateRequest lawyerUpdateRequest =  LawyerSupport.validUpdateRequest();
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();
            Lawyer lawyer = LawyerSupport.validEntity();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.of(lawyer));
            when(lawyerRepository.existsByPhone(lawyerUpdateRequest.phone())).thenReturn(true);

            //Act & Assert
            var exception = assertThrows(PhoneAlreadyExistsException.class, () ->
                    lawyerService.update(lawyerUpdateRequest, jwt));

            verify(lawyerMapper, times(0)).toUpdateLawyer(any(LawyerUpdateRequest.class), any(Lawyer.class));
            assertEquals("Phone already registered: " + lawyerUpdateRequest.phone(), exception.getMessage());
        }
    }

    @Nested
    class DeleteLawyer {
        @Test
        void should_delete_lawyer_with_success() {
            //Arrange
            Jwt jwt = mock(Jwt.class);
            Lawyer lawyer = LawyerSupport.validEntity();
            UUID id = lawyer.getId();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.of(lawyer));
            doNothing().when(fileStorageService).delete(lawyer.getProfilePhotoPath());
            doNothing().when(lawyerRepository).delete(lawyer);

            //Act & Assert
            assertDoesNotThrow(() -> lawyerService.delete(jwt));

            verify(lawyerRepository).findById(id);
            verify(lawyerRepository).delete(lawyerArgumentCaptor.capture());

            var captured = lawyerArgumentCaptor.getValue();

            assertEquals(lawyer.getId(), captured.getId());
        }

        @Test
        void should_throw_NotFoundException_when_lawyer_not_exists_in_db() {
            Jwt jwt = mock(Jwt.class);
            UUID id = UUID.randomUUID();

            when(jwt.getSubject()).thenReturn(id.toString());
            when(lawyerRepository.findById(id)).thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class, () ->
                    lawyerService.delete(jwt));

            verify(lawyerRepository, times(0)).delete(any(Lawyer.class));
            assertEquals("User not found.", exception.getMessage());
        }
    }

}