package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file, UUID userId) throws IOException {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, "cv-files", userId.toString());
            Files.createDirectories(uploadPath);
            
            // Generate unique filename
            String timestamp = String.valueOf(Instant.now().toEpochMilli());
            String fileName = String.format("%s-%s", timestamp, file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            
            // Store file
            Files.copy(file.getInputStream(), filePath);
            
            // Return relative path
            String relativePath = Paths.get("cv-files", userId.toString(), fileName).toString();
            
            log.info("File stored successfully: path={}, size={}", relativePath, file.getSize());
            
            return relativePath;
            
        } catch (Exception e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new IOException("Failed to store file", e);
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        try {
            Path fullPath = Paths.get(uploadDir, filePath);
            Files.deleteIfExists(fullPath);
            log.info("File deleted successfully: path={}", filePath);
        } catch (Exception e) {
            log.error("Failed to delete file: {}", e.getMessage(), e);
            throw new IOException("Failed to delete file", e);
        }
    }

    @Override
    public String generateDownloadUrl(String filePath) {
        // For local storage, return a simple file URL
        // In production, this should be a proper file serving endpoint
        return "/api/files/" + filePath.replace("\\", "/");
    }

    @Override
    public boolean fileExists(String filePath) {
        try {
            Path fullPath = Paths.get(uploadDir, filePath);
            return Files.exists(fullPath);
        } catch (Exception e) {
            log.error("Failed to check if file exists: {}", e.getMessage(), e);
            return false;
        }
    }
}
