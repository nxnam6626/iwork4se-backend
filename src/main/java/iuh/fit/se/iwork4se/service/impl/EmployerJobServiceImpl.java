package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.profile.EmployerJobRequest;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Company;
import iuh.fit.se.iwork4se.model.EmployerProfile;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.repository.EmployerProfileRepository;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.EmployerJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployerJobServiceImpl implements EmployerJobService {

    private final JobRepository jobRepository;
    private final EmployerProfileRepository employerProfileRepository;

    @Override
    public JobDTO createJob(EmployerJobRequest request) {
        Company company = getMyCompanyOrThrow();
        Job job = Job.builder()
                .company(company)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .currency(request.getCurrency())
                .remote(request.isRemote())
                .openings(request.getOpenings())
                .expireAt(request.getExpireAt())
                .build();
        job = jobRepository.save(job);
        return toDTO(job);
    }

    @Override
    public JobDTO updateJob(UUID jobId, EmployerJobRequest request) {
        Job job = getOwnedJob(jobId);
        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getEmploymentType() != null) job.setEmploymentType(request.getEmploymentType());
        if (request.getSalaryMin() != null) job.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) job.setSalaryMax(request.getSalaryMax());
        if (request.getCurrency() != null) job.setCurrency(request.getCurrency());
        job.setRemote(request.isRemote());
        if (request.getOpenings() != null) job.setOpenings(request.getOpenings());
        if (request.getExpireAt() != null) job.setExpireAt(request.getExpireAt());
        job = jobRepository.save(job);
        return toDTO(job);
    }

    @Override
    public void deleteJob(UUID jobId) {
        Job job = getOwnedJob(jobId);
        jobRepository.delete(job);
    }

    @Override
    public Page<JobDTO> listMyCompanyJobs(int page, int size) {
        Company company = getMyCompanyOrThrow();
        var pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        var pageJobs = jobRepository.findByCompany_CompanyIdOrderByCreatedAtDesc(company.getCompanyId(), pageable);
        List<JobDTO> mapped = pageJobs.getContent().stream().map(this::toDTO).toList();
        return new PageImpl<>(mapped, pageable, pageJobs.getTotalElements());
    }

    private Company getMyCompanyOrThrow() {
        UUID userId = SecurityUtil.currentUserId();
        EmployerProfile ep = employerProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ForbiddenException("Employer profile not found"));
        Company company = ep.getCompany();
        if (company == null) throw new ForbiddenException("Company profile is missing");
        return company;
    }

    private Job getOwnedJob(UUID jobId) {
        Company company = getMyCompanyOrThrow();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getCompany().getCompanyId().equals(company.getCompanyId())) {
            throw new ForbiddenException("Not owner of this job");
        }
        return job;
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


