package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.JobSearchRequest;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.service.JobSeekerJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobSeekerJobServiceImpl implements JobSeekerJobService {

    private final JobRepository jobRepository;

    @Override
    public Page<JobDTO> searchJobs(JobSearchRequest request) {
        var pageable = PageRequest.of(Math.max(request.getPage(), 0), Math.max(request.getSize(), 1));
        var page = jobRepository.searchJobs(
                request.getKeyword(),
                request.getLocation(),
                request.getEmploymentType(),
                request.getSalaryMin(),
                request.getSalaryMax(),
                request.getCurrency(),
                request.getRemote(),
                pageable
        );
        List<JobDTO> mapped = page.getContent().stream().map(this::toDTO).toList();
        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }

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
