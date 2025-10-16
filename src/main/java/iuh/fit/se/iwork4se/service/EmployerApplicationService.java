package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.ApplicantSummaryDTO;
import iuh.fit.se.iwork4se.dto.profile.ApplicationDetailDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateApplicationStatusRequest;
import iuh.fit.se.iwork4se.model.ApplicationStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployerApplicationService {
    Page<ApplicantSummaryDTO> listApplicants(Integer page, Integer size, ApplicationStatus status, UUID jobId);
    ApplicationDetailDTO getApplication(UUID applicationId);
    ApplicationDetailDTO updateStatus(UUID applicationId, UpdateApplicationStatusRequest request);
}


