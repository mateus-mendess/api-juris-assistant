package br.com.juristrack.Juris.Track.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3StorageConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean("S3Client")
    @Profile("dev")
    public S3Client s3Client(@Value("${cloud.aws.credentials.secret-key}") String secretKey,
                             @Value("${cloud.aws.credentials.access-key}") String accessKey) {

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(awsBasicCredentials)
                )
                .build();
    }

    @Bean("S3Client")
    @Profile("prod")
    public S3Client s3ClientProd() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

}
