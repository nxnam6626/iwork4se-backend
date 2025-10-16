package iuh.fit.se.iwork4se.dto.profile;

import iuh.fit.se.iwork4se.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSummaryDTO {
    private UUID applicationId;
    private UUID jobId;
    private String jobTitle;
    private UUID jobSeekerProfileId;
    private String candidateName;
    private String candidateHeadline;
    private String candidateLocation;
    private ApplicationStatus status;
}


