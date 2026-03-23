package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.FileRequiredException;
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
public class UploadService {

    private static final String TEMP_DIR = "temp_uploads/";

    private final DocumentsRepository documentsRepository;
    private final DocumentsMapper documentsMapper;
    private final FileStorageService fileStorageService;
    private final ClientService clientService;

    @Transactional
    public UploadResponse upload(UUID clientId, MultipartFile file, FileType fileType) {
        validate(file, fileType);

        Client client = clientService.findById(clientId);

        String relativePath = fileStorageService.save(file, fileType);

        Document documents = documentsMapper.toDocument(file.getOriginalFilename(), relativePath, fileType);
        documents.linkClient(client);

        Document document = documentsRepository.save(documents);

        return documentsMapper.toUploadResponse(document.getId(), document.getFilePath());
    }

    private void validate(MultipartFile file, FileType type) {
        if (type == FileType.POWER_OF_ATTORNEY && file.isEmpty()) {
            throw new FileRequiredException("Required file for registration.");
        }
    }
}
