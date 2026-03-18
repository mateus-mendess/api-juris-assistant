package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.FileRequiredException;
import br.com.juristrack.Juris.Track.mapper.DocumentsMapper;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Document;
import br.com.juristrack.Juris.Track.model.repository.DocumentsRepository;
import br.com.juristrack.Juris.Track.support.ClientSupport;
import br.com.juristrack.Juris.Track.support.DocumentsSupport;
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
class UploadServiceTest {

    @Mock
    private DocumentsRepository documentsRepository;

    @Mock
    private DocumentsMapper documentsMapper;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private UploadService uploadService;

    @Nested
    class UploadFile {
        @Test
        void should_upload_file_when_exists() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client client = ClientSupport.validEntity();
            Document documents = DocumentsSupport.validEntity(client);

            when(clientService.findById(client.getId())).thenReturn(client);
            when(fileStorageService.save(any(MultipartFile.class), any(FileType.class))).thenReturn("/folder/filename");
            when(documentsMapper.toDocument(any(String.class), any(String.class), any(FileType.class))).thenReturn(documents);
            when(documentsRepository.save(documents)).thenReturn(documents);

            //Act & Assert
            assertDoesNotThrow(() -> uploadService.upload(client.getId(), documents.getFileName(), file, FileType.CONTRACT));

            verify(fileStorageService).save(any(MultipartFile.class), any(FileType.class));
            verify(documentsRepository).save(any(Document.class));

            assertNotNull(documents.getClient());
        }

        @Test
        void should_throw_FileRequiredException_when_file_is_empty_and_is_type_power_of_attorney() {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);

            when(file.isEmpty()).thenReturn(true);

            //Act & Assert
            var result = assertThrows(FileRequiredException.class, () -> uploadService.upload(UUID.randomUUID(), "title", file, FileType.POWER_OF_ATTORNEY));

            verify(fileStorageService, times(0)).save(any(MultipartFile.class), any(FileType.class));
            verify(documentsRepository, times(0)).save(any(Document.class));

            assertEquals("Required file for registration.", result.getMessage());
        }
    }

}