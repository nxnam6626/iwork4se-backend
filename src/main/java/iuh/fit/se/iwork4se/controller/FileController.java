package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.service.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final SupabaseStorageService supabaseStorageService;

    /**
     * Upload avatar image to Supabase Storage
     */
    @PostMapping("/avatar/upload")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            // Validate file type for avatar (images only)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed for avatar"));
            }

            String fileName = supabaseStorageService.uploadFile(file, "avatars");
            String publicUrl = supabaseStorageService.getPublicUrl(fileName, "avatars");

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("publicUrl", publicUrl);
            response.put("message", "Avatar uploaded successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to upload avatar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload avatar: " + e.getMessage()));
        }
    }

    /**
     * Upload resume/CV file to Supabase Storage
     */
    @PostMapping("/resumes/upload")
    public ResponseEntity<Map<String, String>> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            // Validate file type for resume (PDF, DOC, DOCX)
            String contentType = file.getContentType();
            if (contentType == null || 
                (!contentType.equals("application/pdf") && 
                 !contentType.equals("application/msword") && 
                 !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PDF, DOC, and DOCX files are allowed for resumes"));
            }

            String fileName = supabaseStorageService.uploadFile(file, "resumes");
            String publicUrl = supabaseStorageService.getPublicUrl(fileName, "resumes");

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("publicUrl", publicUrl);
            response.put("message", "Resume uploaded successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to upload resume: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload resume: " + e.getMessage()));
        }
    }

    /**
     * Delete avatar from Supabase Storage
     */
    @DeleteMapping("/avatar/{fileName}")
    public ResponseEntity<Map<String, String>> deleteAvatar(@PathVariable String fileName) {
        try {
            boolean deleted = supabaseStorageService.deleteFile(fileName, "avatar");
            
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Avatar deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Avatar not found or failed to delete"));
            }

        } catch (Exception e) {
            log.error("Failed to delete avatar: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete avatar: " + e.getMessage()));
        }
    }

    /**
     * Delete resume from Supabase Storage
     */
    @DeleteMapping("/resumes/{fileName}")
    public ResponseEntity<Map<String, String>> deleteResume(@PathVariable String fileName) {
        try {
            boolean deleted = supabaseStorageService.deleteFile(fileName, "resumes");
            
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Resume not found or failed to delete"));
            }

        } catch (Exception e) {
            log.error("Failed to delete resume: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete resume: " + e.getMessage()));
        }
    }

    /**
     * Get public URL for avatar
     */
    @GetMapping("/avatar/{fileName}/url")
    public ResponseEntity<Map<String, String>> getAvatarUrl(@PathVariable String fileName) {
        try {
            String publicUrl = supabaseStorageService.getPublicUrl(fileName, "avatar");
            return ResponseEntity.ok(Map.of("publicUrl", publicUrl));

        } catch (Exception e) {
            log.error("Failed to get avatar URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get avatar URL: " + e.getMessage()));
        }
    }

    /**
     * Get public URL for resume
     */
    @GetMapping("/resumes/{fileName}/url")
    public ResponseEntity<Map<String, String>> getResumeUrl(@PathVariable String fileName) {
        try {
            String publicUrl = supabaseStorageService.getPublicUrl(fileName, "resumes");
            return ResponseEntity.ok(Map.of("publicUrl", publicUrl));

        } catch (Exception e) {
            log.error("Failed to get resume URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get resume URL: " + e.getMessage()));
        }
    }
}
