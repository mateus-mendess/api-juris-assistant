package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.config.FileStorageConfig;
import br.com.juristrack.Juris.Track.exception.UploadFailException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getStoragePath()).toAbsolutePath().normalize();
    }

    public String save(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new UploadFailException("Empty file.");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String relativePath = folder + UUID.randomUUID() + "-" + fileName;

        try {
            Path targetLocation = fileStorageLocation.resolve(relativePath).normalize();

            if (!targetLocation.startsWith(fileStorageLocation)) {
                throw new UploadFailException("invalid file.");
            }

            file.transferTo(targetLocation);

            return relativePath;
        } catch (IOException exception) {
            throw new UploadFailException("Failed to save file: " + exception);
        }
    }
}
