package de.ait.platform.user.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

    public SpaceService( ) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                "${SPACES_ACCESS_KEY}",
                "${SPACES_SECRET_KEY}"
        );
        System.out.println("${SPACES_ACCESS_KEY}");
        System.out.println("${SPACES_SECRET_KEY}");

        this.s3 = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of("fra1")) // Замените на регион вашего Space
                .build();
                AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://fra1.digitaloceanspaces.com", "fra1"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("{SPACES_ACCESS_KEY}", "{SPACES_SECRET_KEY}")))
                .build();
    }
    public String uploadFile(String fileName, Path filePath) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("${SPACES_BUCKET}")
                    .key(fileName)
                    .build();
            s3.putObject(putObjectRequest, filePath);
            return "https://${SPACES_ENDPOINT}.digitaloceanspaces.com/" + fileName;
        } catch (S3Exception e) {
            throw new RuntimeException("Error uploading file to Spaces: " + e.getMessage());
        }
    }
}

