package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public JobDTO getJobDetail(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return toDTO(job);
    }

    private JobDTO toDTO(Job j) {
        return JobDTO.builder()
                .jobId(j.getJobId())
                .companyId(j.getCompany() != null ? j.getCompany().getCompanyId() : null)
                .companyName(j.getCompany() != null ? j.getCompany().getName() : null)
                .title(j.getTitle())
                .description(j.getDescription())
                .location(j.getLocation())
                .employmentType(j.getEmploymentType())
                .salaryMin(j.getSalaryMin())
                .salaryMax(j.getSalaryMax())
                .currency(j.getCurrency())
                .remote(j.isRemote())
                .openings(j.getOpenings())
                .expireAt(j.getExpireAt())
                .build();
    }
}


