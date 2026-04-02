package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.mapper.DocumentsMapper;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Document;
import br.com.juristrack.Juris.Track.model.repository.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentsRepository documentsRepository;
    private final DocumentsMapper documentsMapper;
    private final FileStorageService fileStorageService;
    private final ClientService clientService;

    @Transactional
    public UploadResponse uploadFileFromS3(UUID clientId, MultipartFile file, FileType fileType) throws Exception {
        Client client = clientService.findById(clientId);

        String relativePath = fileStorageService.uploadFile(file, fileType);

        Document documents = documentsMapper.toDocument(file.getOriginalFilename(), relativePath, fileType);
        documents.linkClient(client);

        Document document = documentsRepository.save(documents);

        return documentsMapper.toUploadResponse(document.getId(), document.getFilePath());
    }

    @Transactional
    public void removeFileFromS3(UUID documentId) {
        Document document = documentsRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document not found"));

        if (document.getFilePath() == null || document.getFilePath().isEmpty()) {
            return;
        }

        fileStorageService.removeFile(document.getFilePath());

        documentsRepository.delete(document);
    }
}
