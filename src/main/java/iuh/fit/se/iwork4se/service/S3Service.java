package iuh.fit.se.iwork4se.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface S3Service {
    
    /**
     * Upload file to S3 and return the object key
     * @param file MultipartFile to upload
     * @param userId User ID for organizing files
     * @return Object key (S3 path) of the uploaded file
     * @throws IOException if upload fails
     */
    String uploadFile(MultipartFile file, UUID userId) throws IOException;
    
    /**
     * Delete file from S3
     * @param objectKey S3 object key to delete
     * @throws IOException if deletion fails
     */
    void deleteFile(String objectKey) throws IOException;
    
    /**
     * Generate presigned URL for file download
     * @param objectKey S3 object key
     * @return Presigned URL for downloading the file
     */
    String generatePresignedUrl(String objectKey);
    
    /**
     * Check if file exists in S3
     * @param objectKey S3 object key
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String objectKey);
}
