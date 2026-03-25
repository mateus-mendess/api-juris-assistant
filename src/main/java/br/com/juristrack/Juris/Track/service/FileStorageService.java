package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.config.FileStorageConfig;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig, S3Client s3Client) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getStoragePath())
                .toAbsolutePath()
                .normalize();
        this.s3Client = s3Client;

    }

    public String save(MultipartFile file, FileType fileType) {
        if (file.isEmpty()) {
            return null;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String relativePath = fileType.getFolder() + UUID.randomUUID() + "-" + fileName;

        try {
            Path targetLocation = fileStorageLocation.resolve(relativePath).normalize();

            if (!targetLocation.startsWith(fileStorageLocation)) {
                throw new FileStorageException("invalid file.");
            }

            file.transferTo(targetLocation);

            return relativePath;
        } catch (IOException exception) {
            throw new FileStorageException("Failed to buildAddress file: " + exception);
        }
    }

    public String uploadS3(MultipartFile file, FileType fileType) throws Exception {
        String key = fileType.getFolder() + UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return key;
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
