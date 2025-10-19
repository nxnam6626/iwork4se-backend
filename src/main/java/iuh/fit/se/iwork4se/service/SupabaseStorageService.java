package iuh.fit.se.iwork4se.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface SupabaseStorageService {
    
    /**
     * Upload file to Supabase Storage bucket
     * @param file MultipartFile to upload
     * @param bucketName Name of the bucket (avatar or resumes)
     * @return URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String bucketName);
    
    /**
     * Delete file from Supabase Storage bucket
     * @param fileName Name of the file to delete
     * @param bucketName Name of the bucket
     * @return true if deletion was successful
     */
    boolean deleteFile(String fileName, String bucketName);
    
    /**
     * Get public URL of a file
     * @param fileName Name of the file
     * @param bucketName Name of the bucket
     * @return Public URL of the file
     */
    String getPublicUrl(String fileName, String bucketName);
    
    /**
     * Generate unique filename with UUID
     * @param originalFilename Original filename
     * @return Unique filename with UUID prefix
     */
    default String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
