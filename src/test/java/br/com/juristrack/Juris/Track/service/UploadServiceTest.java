package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.enums.FileType;
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
    private DocumentService uploadService;

    @Nested
    class UploadFile {
        @Test
        void should_upload_file_when_exists() throws Exception {
            //Arrange
            MultipartFile file = mock(MultipartFile.class);
            Client client = ClientSupport.validEntity();
            Document documents = DocumentsSupport.validEntity(client);

            when(file.getOriginalFilename()).thenReturn("file.png");
            when(clientService.findById(client.getId())).thenReturn(client);
            when(fileStorageService.uploadFile(any(MultipartFile.class), any(FileType.class))).thenReturn("/folder/filename");
            when(documentsMapper.toDocument(any(String.class), any(String.class), any(FileType.class))).thenReturn(documents);
            when(documentsRepository.save(any(Document.class))).thenReturn(documents);

            //Act & Assert
            assertDoesNotThrow(() -> uploadService.uploadFileFromS3(client.getId(), file, FileType.CONTRACT));

            verify(fileStorageService).uploadFile(any(MultipartFile.class), any(FileType.class));
            verify(documentsRepository).save(any(Document.class));

            assertNotNull(documents.getClient());
        }
    }
}