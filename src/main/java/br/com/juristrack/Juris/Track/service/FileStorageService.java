package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.enums.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileStorageService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

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
}
