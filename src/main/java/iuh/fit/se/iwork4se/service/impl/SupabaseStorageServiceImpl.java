package iuh.fit.se.iwork4se.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.iwork4se.config.SupabaseConfig;
import iuh.fit.se.iwork4se.service.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageServiceImpl implements SupabaseStorageService {

    private final SupabaseConfig supabaseConfig;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        try {
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
            
            String url = supabaseConfig.getSupabaseUrl() + "/storage/v1/object/" + bucketName + "/" + uniqueFileName;
            
            RequestBody requestBody = RequestBody.create(
                file.getBytes(),
                MediaType.parse(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
            );
            
            Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseServiceRoleKey())
                .header("Content-Type", file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .post(requestBody)
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("File uploaded successfully: {} to bucket: {}", uniqueFileName, bucketName);
                    return uniqueFileName;
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Failed to upload file: {} - {}", response.code(), errorBody);
                    throw new RuntimeException("Failed to upload file: " + response.code() + " - " + errorBody);
                }
            }
            
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public boolean deleteFile(String fileName, String bucketName) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/storage/v1/object/" + bucketName + "/" + fileName;
            
            Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseServiceRoleKey())
                .delete()
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("File deleted successfully: {} from bucket: {}", fileName, bucketName);
                    return true;
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Failed to delete file: {} - {}", response.code(), errorBody);
                    return false;
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to delete file: {} from bucket: {}", fileName, bucketName, e);
            return false;
        }
    }

    @Override
    public String getPublicUrl(String fileName, String bucketName) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/storage/v1/object/public/" + bucketName + "/" + fileName;
            return url;
            
        } catch (Exception e) {
            log.error("Failed to get public URL for file: {} from bucket: {}", fileName, bucketName, e);
            throw new RuntimeException("Failed to get public URL", e);
        }
    }
}
