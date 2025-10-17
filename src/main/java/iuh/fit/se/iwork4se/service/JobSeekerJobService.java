package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.JobSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface JobSeekerJobService {
    Page<JobDTO> searchJobs(JobSearchRequest request);
    JobDTO getJobDetail(UUID jobId);
}
