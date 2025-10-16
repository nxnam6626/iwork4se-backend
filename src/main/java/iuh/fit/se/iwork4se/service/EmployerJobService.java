package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.profile.EmployerJobRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployerJobService {
    JobDTO createJob(EmployerJobRequest request);
    JobDTO updateJob(UUID jobId, EmployerJobRequest request);
    void deleteJob(UUID jobId);
    Page<JobDTO> listMyCompanyJobs(int page, int size);
}


