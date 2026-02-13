package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.config.FileStorageConfig;
import br.com.juristrack.Juris.Track.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final String folderPath;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getStoragePath())
                .toAbsolutePath()
                .normalize();

        this.folderPath = fileStorageConfig.getPhotoFolder();
    }

    public String save(MultipartFile filePhoto) {
        if (filePhoto.isEmpty()) {
            return null;
        }

        String fileName = StringUtils.cleanPath(filePhoto.getOriginalFilename());
        String relativePath = folderPath + UUID.randomUUID() + "-" + fileName;

        try {
            Path targetLocation = fileStorageLocation.resolve(relativePath).normalize();

            if (!targetLocation.startsWith(fileStorageLocation)) {
                throw new FileStorageException("invalid filePhoto.");
            }

            filePhoto.transferTo(targetLocation);

            return relativePath;
        } catch (IOException exception) {
            throw new FileStorageException("Failed to save filePhoto: " + exception);
        }
    }

    public String update(MultipartFile filePhoto, String relativePath) {
        if (filePhoto == null || filePhoto.isEmpty()) {
            return null;
        }

        if (relativePath != null && !relativePath.isBlank()) {
            delete(relativePath);
        }

        return save(filePhoto);
    }

    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }

        try {
            Path targetLocation = fileStorageLocation.resolve(relativePath).normalize();

            Files.deleteIfExists(targetLocation);
        } catch (IOException exception) {
            throw new FileStorageException("Error deleting photo file: " + exception);
        }

    }
}
