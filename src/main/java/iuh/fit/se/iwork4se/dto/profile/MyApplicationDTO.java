package iuh.fit.se.iwork4se.dto.profile;

import iuh.fit.se.iwork4se.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationDTO {
    private UUID applicationId;
    private UUID jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private ApplicationStatus status;
    private String note;
    private Instant appliedAt;
}
