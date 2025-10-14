package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record CreateCVFileRequest(
        UUID resumeId,                 // có thể null nếu chỉ upload file chưa gắn resume
        @NotBlank String objectKey,
        @NotBlank String fileName,
        String mimeType,
        @Min(0) long size
) {}
