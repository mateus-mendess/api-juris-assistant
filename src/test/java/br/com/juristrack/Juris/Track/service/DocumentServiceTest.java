package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.FileRequiredException;
import br.com.juristrack.Juris.Track.mapper.DocumentMapper;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Contract;
import br.com.juristrack.Juris.Track.model.entity.DeclarationTerm;
import br.com.juristrack.Juris.Track.model.entity.PowerOfAttorney;
import br.com.juristrack.Juris.Track.model.repository.ContractRepository;
import br.com.juristrack.Juris.Track.model.repository.DeclarationTermRepository;
import br.com.juristrack.Juris.Track.model.repository.PowerOfAttorneyRepository;
import br.com.juristrack.Juris.Track.support.ClientSupport;
import br.com.juristrack.Juris.Track.support.ContractSupport;
import br.com.juristrack.Juris.Track.support.DeclarationTermSupport;
import br.com.juristrack.Juris.Track.support.PowerOfAttorneySupport;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @Mock
    private PowerOfAttorneyRepository powerOfAttorneyRepository;

    @Mock
    private DeclarationTermRepository declarationTermRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private DocumentService documentService;

    @Nested
    class UploadPowerOfAttorney {
        @Test
        void should_upload_when_file_exists() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client clientEntity = ClientSupport.validEntity();
            PowerOfAttorney powerOfAttorney = PowerOfAttorneySupport.validEntity(clientEntity);

            when(clientService.findById(clientEntity.getId())).thenReturn(clientEntity);
            when(file.isEmpty()).thenReturn(false);
            when(fileStorageService.save(file, FileType.POWER_OF_ATTORNEY)).thenReturn("/folde/file");
            when(documentMapper.toPowerOfAttorney(any(String.class), any(String.class))).thenReturn(powerOfAttorney);
            when(powerOfAttorneyRepository.save(powerOfAttorney)).thenReturn(powerOfAttorney);

            //Act & Assert
            assertDoesNotThrow(() -> documentService.uploadPowerOfAttorney(clientEntity.getId(), powerOfAttorney.getName(), file));

            verify(fileStorageService).save(any(MultipartFile.class), any(FileType.class));
            verify(powerOfAttorneyRepository).save(any(PowerOfAttorney.class));
        }

        @Test
        void should_throw_FileRequiredException_when_file_not_exists() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client clientEntity = ClientSupport.validEntity();

            when(clientService.findById(clientEntity.getId())).thenReturn(clientEntity);
            when(file.isEmpty()).thenReturn(true);

            //Act & Assert
            var result = assertThrows(FileRequiredException.class, () -> documentService.uploadPowerOfAttorney(clientEntity.getId(), "nameFile", file));

            verify(fileStorageService, times(0)).save(any(MultipartFile.class), any(FileType.class));
            verify(powerOfAttorneyRepository, times(0)).save(any(PowerOfAttorney.class));

            assertEquals("Power of Attorney required.", result.getMessage());
        }
    }

    @Nested
    class UploadDeclarationTerm {
        @Test
        void should_upload_when_file_exists() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client clientEntity = ClientSupport.validEntity();
            DeclarationTerm declarationTerm = DeclarationTermSupport.validEntity(clientEntity);

            when(clientService.findById(clientEntity.getId())).thenReturn(clientEntity);
            when(fileStorageService.save(file, FileType.DECLARATION_TERM)).thenReturn("/folde/file");
            when(documentMapper.toDeclarationTerm(any(String.class), any(String.class))).thenReturn(declarationTerm);
            when(declarationTermRepository.save(declarationTerm)).thenReturn(declarationTerm);

            //Act & Assert
            assertDoesNotThrow(() -> documentService.uploadDeclarationTerm(clientEntity.getId(), "nameFile", file));

            verify(fileStorageService).save(any(MultipartFile.class), any(FileType.class));
            verify(declarationTermRepository).save(any(DeclarationTerm.class));
        }
    }

    @Nested
    class UploadContract {
        @Test
        void should_upload_when_file_exists() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client clientEntity = ClientSupport.validEntity();
            Contract contract = ContractSupport.validEntity(clientEntity);

            when(clientService.findById(clientEntity.getId())).thenReturn(clientEntity);
            when(fileStorageService.save(file, FileType.CONTRACT)).thenReturn("/folde/file");
            when(documentMapper.toContract(any(String.class), any(String.class))).thenReturn(contract);
            when(contractRepository.save(contract)).thenReturn(contract);

            //Act & Assert
            assertDoesNotThrow(() -> documentService.uploadContract(clientEntity.getId(), "nameFile", file));

            verify(fileStorageService).save(any(MultipartFile.class), any(FileType.class));
            verify(contractRepository).save(any(Contract.class));
        }
    }

}