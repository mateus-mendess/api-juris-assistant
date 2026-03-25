package br.com.juristrack.Juris.Track.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${aws.bucket.name}")
    private final String bucket;

    private final S3Client s3Client;

    public void uploadS3() {
        return;
    }
}
