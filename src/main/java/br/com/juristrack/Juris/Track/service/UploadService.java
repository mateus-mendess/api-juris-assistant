package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.enums.FileType;
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

    private final DocumentsRepository documentsRepository;
    private final DocumentsMapper documentsMapper;
    private final FileStorageService fileStorageService;
    private final ClientService clientService;

    @Transactional
    public UploadResponse upload(UUID clientId, MultipartFile file, FileType fileType) throws Exception {
        Client client = clientService.findById(clientId);

        String relativePath = fileStorageService.uploadS3(file, fileType);

        Document documents = documentsMapper.toDocument(file.getOriginalFilename(), relativePath, fileType);
        documents.linkClient(client);

        Document document = documentsRepository.save(documents);

        return documentsMapper.toUploadResponse(document.getId(), document.getFilePath());
    }
}
