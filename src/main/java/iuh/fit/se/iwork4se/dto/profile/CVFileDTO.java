package iuh.fit.se.iwork4se.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CVFileDTO {
    private UUID id;
    private UUID ownerUserId;
    private String objectKey;
    private String fileName;
    private String mimeType;
    private long size;
    private Instant uploadedAt;
    private UUID resumeId;
}
