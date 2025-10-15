package iuh.fit.se.iwork4se.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import iuh.fit.se.iwork4se.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, UUID userId) throws IOException {
        try {
            // Generate unique object key: cv-files/{userId}/{timestamp}-{originalFileName}
            String timestamp = String.valueOf(Instant.now().toEpochMilli());
            String objectKey = String.format("cv-files/%s/%s-%s", 
                userId.toString(), timestamp, file.getOriginalFilename());
            
            // Create object metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            metadata.addUserMetadata("uploaded-by", userId.toString());
            metadata.addUserMetadata("original-filename", file.getOriginalFilename());
            metadata.addUserMetadata("upload-timestamp", timestamp);
            
            // Upload file to S3
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, 
                objectKey, 
                file.getInputStream(), 
                metadata
            );
            
            // Set ACL to private (default)
            putObjectRequest.setCannedAcl(CannedAccessControlList.Private);
            
            amazonS3.putObject(putObjectRequest);
            
            log.info("File uploaded successfully to S3: bucket={}, key={}, size={}", 
                bucketName, objectKey, file.getSize());
            
            return objectKey;
            
        } catch (Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to S3", e);
        }
    }

    @Override
    public void deleteFile(String objectKey) throws IOException {
        try {
            amazonS3.deleteObject(bucketName, objectKey);
            log.info("File deleted successfully from S3: bucket={}, key={}", bucketName, objectKey);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage(), e);
            throw new IOException("Failed to delete file from S3", e);
        }
    }

    @Override
    public String generatePresignedUrl(String objectKey) {
        try {
            // Generate presigned URL valid for 1 hour
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60; // 1 hour
            expiration.setTime(expTimeMillis);
            
            GeneratePresignedUrlRequest generatePresignedUrlRequest = 
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                    .withMethod(com.amazonaws.HttpMethod.GET)
                    .withExpiration(expiration);
            
            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            log.error("Failed to generate presigned URL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public boolean fileExists(String objectKey) {
        try {
            return amazonS3.doesObjectExist(bucketName, objectKey);
        } catch (Exception e) {
            log.error("Failed to check if file exists in S3: {}", e.getMessage(), e);
            return false;
        }
    }
}
