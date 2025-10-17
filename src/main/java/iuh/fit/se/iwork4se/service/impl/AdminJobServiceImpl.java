package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.PublicJobSearchRequest;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.service.AdminJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminJobServiceImpl implements AdminJobService {

    private final JobRepository jobRepository;

    @Override
    public Page<JobDTO> getAllJobs(PublicJobSearchRequest request) {
        Pageable pageable = createPageable(request);
        Specification<Job> spec = createSearchSpecification(request);
        
        Page<Job> jobs = jobRepository.findAll(spec, pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public JobDTO getJobDetail(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        return toJobDTO(job);
    }

    @Override
    @Transactional
    public JobDTO toggleJobVisibility(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        // Toggle visibility - giả sử có field active trong Job model
        // job.setActive(!job.isActive());
        // job = jobRepository.save(job);
        
        return toJobDTO(job);
    }

    @Override
    @Transactional
    public void deleteJob(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        jobRepository.delete(job);
    }

    @Override
    public long getJobCountByStatus(String status) {
        // Giả sử có field status trong Job model
        // return jobRepository.countByStatus(status);
        return jobRepository.count(); // Temporary implementation
    }

    @Override
    public Page<JobDTO> getExpiringJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "expireAt"));
        LocalDate threshold = LocalDate.now().plusDays(7); // Jobs expiring in next 7 days
        
        Page<Job> jobs = jobRepository.findByExpireAtBeforeAndExpireAtAfter(
                threshold, LocalDate.now(), pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> getJobsByCompany(UUID companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobs = jobRepository.findByCompany_CompanyIdOrderByCreatedAtDesc(companyId, pageable);
        
        return jobs.map(this::toJobDTO);
    }

    private Pageable createPageable(PublicJobSearchRequest request) {
        Sort sort = createSort(request.getSortBy(), request.getSortDirection());
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        
        switch (sortBy != null ? sortBy.toLowerCase() : "createdat") {
            case "salary":
                return Sort.by(direction, "salaryMax");
            case "title":
                return Sort.by(direction, "title");
            case "company":
                return Sort.by(direction, "company.name");
            case "expire":
                return Sort.by(direction, "expireAt");
            default:
                return Sort.by(direction, "createdAt");
        }
    }

    private Specification<Job> createSearchSpecification(PublicJobSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword search in title and description
            if (StringUtils.hasText(request.getKeyword())) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), keyword);
                Predicate descPredicate = cb.like(cb.lower(root.get("description")), keyword);
                predicates.add(cb.or(titlePredicate, descPredicate));
            }

            // Location filter
            if (StringUtils.hasText(request.getLocation())) {
                predicates.add(cb.like(cb.lower(root.get("location")), 
                    "%" + request.getLocation().toLowerCase() + "%"));
            }

            // Employment type filter
            if (StringUtils.hasText(request.getEmploymentType())) {
                predicates.add(cb.equal(root.get("employmentType"), request.getEmploymentType()));
            }

            // Salary range filter
            if (request.getSalaryMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMax"), request.getSalaryMin()));
            }
            if (request.getSalaryMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaryMin"), request.getSalaryMax()));
            }

            // Currency filter
            if (StringUtils.hasText(request.getCurrency())) {
                predicates.add(cb.equal(root.get("currency"), request.getCurrency()));
            }

            // Remote work filter
            if (request.getRemote() != null) {
                predicates.add(cb.equal(root.get("remote"), request.getRemote()));
            }

            // Company name filter
            if (StringUtils.hasText(request.getCompanyName())) {
                predicates.add(cb.like(cb.lower(root.get("company").get("name")), 
                    "%" + request.getCompanyName().toLowerCase() + "%"));
            }

            // Industry filter
            if (StringUtils.hasText(request.getIndustry())) {
                predicates.add(cb.like(cb.lower(root.get("company").get("industry")), 
                    "%" + request.getIndustry().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private JobDTO toJobDTO(Job job) {
        return JobDTO.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .remote(job.isRemote())
                .companyName(job.getCompany().getName())
                .companyId(job.getCompany().getCompanyId())
                .expireAt(job.getExpireAt())
                .build();
    }
}
