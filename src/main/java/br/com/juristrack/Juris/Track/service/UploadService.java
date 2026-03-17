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
    public void upload(UUID id, String fileName, MultipartFile file, FileType type) {
        validate(file, type);

        Client client = clientService.findById(id);

        String relativePath = fileStorageService.save(file, type);

        Document documents = documentsMapper.toDocument(fileName, relativePath, type);
        documents.linkClient(client);

        documentsRepository.save(documents);
    }

    public void uploadChunk(String fileName, MultipartFile chunk, Integer chunkIndex) throws IOException {
        Path dir = Paths.get(TEMP_DIR, fileName);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        Path chunkFile = dir.resolve("chunk_" + chunkIndex);

        try(OutputStream os = Files.newOutputStream(chunkFile)) {
            os.write(chunk.getBytes());
        }
    }

    public void mergeChunk(String fileName) throws IOException {
        File dir = new File(TEMP_DIR + fileName);
        File mergedFile = new File("test-chunk/" + fileName);

        mergedFile.getParentFile().mkdirs();

        try (OutputStream os = new FileOutputStream(mergedFile)) {

            for (int i = 0; i < dir.listFiles().length; i++) {
                File chunkFile = new File(dir, "chunk_" + i);
                Files.copy(chunkFile.toPath(), os);
                chunkFile.delete();
            }

        }
        dir.delete();
    }

    private void validate(MultipartFile file, FileType type) {
        if (type == FileType.POWER_OF_ATTORNEY && file.isEmpty()) {
            throw new FileRequiredException("Required file for registration.");
        }
    }
}
