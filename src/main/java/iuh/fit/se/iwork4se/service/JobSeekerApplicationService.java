package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.ApplyJobRequest;
import iuh.fit.se.iwork4se.dto.profile.MyApplicationDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface JobSeekerApplicationService {
    MyApplicationDTO applyToJob(UUID jobId, ApplyJobRequest request);
    Page<MyApplicationDTO> getMyApplications(int page, int size);
    MyApplicationDTO getMyApplication(UUID applicationId);
}
