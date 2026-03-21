package br.com.juristrack.Juris.Track.service;

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

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String upload(UUID id, String fileName, MultipartFile file, FileType type) {
        validate(file, type);

        Client client = clientService.findById(id);

        String relativePath = fileStorageService.save(file, type);

        Document documents = documentsMapper.toDocument(fileName, relativePath, type);
        documents.linkClient(client);

        documentsRepository.save(documents);

        return relativePath;
    }

    private void validate(MultipartFile file, FileType type) {
        if (type == FileType.POWER_OF_ATTORNEY && file.isEmpty()) {
            throw new FileRequiredException("Required file for registration.");
        }
    }
}
