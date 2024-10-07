package de.ait.platform.user.service;


import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Path;

@Service
public class SpaceService {

    private final S3Client s3;

    public SpaceService() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "<ваш Access Key>",
                "<ваш Secret Key>"
        );

        this.s3 = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_1) // Замените на регион вашего Space
                .build();
    }

    public String uploadFile(String fileName, Path filePath) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("<имя вашего Space>")
                    .key(fileName)
                    .build();

            s3.putObject(putObjectRequest, filePath);
            return "https://<ваш-space-region>.digitaloceanspaces.com/" + fileName;

        } catch (S3Exception e) {
            throw new RuntimeException("Error uploading file to Spaces: " + e.getMessage());
        }
    }
}

