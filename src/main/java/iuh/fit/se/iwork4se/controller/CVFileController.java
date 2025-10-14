package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CreateCVFileRequest;
import iuh.fit.se.iwork4se.dto.profile.CVFileDTO;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.CVFileService;
import iuh.fit.se.iwork4se.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/profile/cv-files")
@RequiredArgsConstructor
@Slf4j
public class CVFileController {

    private final CVFileService cvFileService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<CVFileDTO>> listMine() {
        return ResponseEntity.ok(cvFileService.listMyCVFiles(SecurityUtil.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<CVFileDTO> create(@Valid @RequestBody CreateCVFileRequest req) {
        CVFileDTO created = cvFileService.createMyCVFile(
                SecurityUtil.currentUserId(),
                req.resumeId(), req.objectKey(), req.fileName(), req.mimeType(), req.size()
        );
        return ResponseEntity.created(URI.create("/api/job-seeker/profile/cv-files/" + created.getId())).body(created);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CVFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "resumeId", required = false) UUID resumeId) {
        
        UUID currentUserId = SecurityUtil.currentUserId();
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check file type (only PDF and DOC files)
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("application/pdf") && 
                !contentType.equals("application/msword") && 
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                return ResponseEntity.badRequest().build();
            }
            
            // Store file
            String filePath = fileStorageService.storeFile(file, currentUserId);
            
            // Save metadata to database
            CVFileDTO created = cvFileService.createMyCVFile(
                    currentUserId,
                    resumeId, 
                    filePath, 
                    file.getOriginalFilename(), 
                    file.getContentType(), 
                    file.getSize()
            );
            
            log.info("CV file uploaded successfully: userId={}, fileName={}, filePath={}", 
                currentUserId, file.getOriginalFilename(), filePath);
            
            return ResponseEntity.created(URI.create("/api/job-seeker/profile/cv-files/" + created.getId())).body(created);
            
        } catch (IOException e) {
            log.error("Failed to upload CV file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CVFileDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(cvFileService.getMyCVFile(SecurityUtil.currentUserId(), id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<String> getDownloadUrl(@PathVariable UUID id) {
        CVFileDTO cvFile = cvFileService.getMyCVFile(SecurityUtil.currentUserId(), id);
        String downloadUrl = fileStorageService.generateDownloadUrl(cvFile.getObjectKey());
        return ResponseEntity.ok(downloadUrl);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            CVFileDTO cvFile = cvFileService.getMyCVFile(SecurityUtil.currentUserId(), id);
            
            // Delete from storage
            fileStorageService.deleteFile(cvFile.getObjectKey());
            
            // Delete from database
            cvFileService.deleteMyCVFile(SecurityUtil.currentUserId(), id);
            
            log.info("CV file deleted successfully: userId={}, fileId={}, filePath={}", 
                SecurityUtil.currentUserId(), id, cvFile.getObjectKey());
            
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("Failed to delete CV file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
