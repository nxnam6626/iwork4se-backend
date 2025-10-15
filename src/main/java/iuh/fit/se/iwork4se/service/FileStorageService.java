package iuh.fit.se.iwork4se.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileStorageService {
    
    /**
     * Store file and return the file path
     * @param file MultipartFile to store
     * @param userId User ID for organizing files
     * @return File path where the file is stored
     * @throws IOException if storage fails
     */
    String storeFile(MultipartFile file, UUID userId) throws IOException;
    
    /**
     * Delete file from storage
     * @param filePath File path to delete
     * @throws IOException if deletion fails
     */
    void deleteFile(String filePath) throws IOException;
    
    /**
     * Generate download URL for file
     * @param filePath File path
     * @return Download URL
     */
    String generateDownloadUrl(String filePath);
    
    /**
     * Check if file exists in storage
     * @param filePath File path
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String filePath);
}
