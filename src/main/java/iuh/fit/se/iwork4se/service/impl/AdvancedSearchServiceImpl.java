package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.service.AdvancedSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvancedSearchServiceImpl implements AdvancedSearchService {

    private final JobRepository jobRepository;

    @Override
    public Page<JobDTO> fullTextSearch(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // Simple full-text search implementation
        // In production, consider using Elasticsearch or similar
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobsBySkills(List<String> skills, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // TODO: Implement skills-based search when JobSkill model is available
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobsByLocation(String location, double radiusKm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // TODO: Implement location-based search with radius calculation
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobsBySalaryAndBenefits(Integer minSalary, Integer maxSalary, 
                                                      List<String> benefits, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "salaryMax"));
        
        // TODO: Implement salary and benefits search
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobsByCompanyCulture(List<String> cultureKeywords, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // TODO: Implement company culture search
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobsByWorkArrangement(Boolean remote, Boolean hybrid, 
                                                    Boolean onSite, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // TODO: Implement work arrangement search
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public Page<JobDTO> getRecommendedJobs(UUID jobSeekerProfileId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // TODO: Implement AI-powered job recommendations
        // This would analyze job seeker's profile, skills, preferences, and application history
        Page<Job> jobs = jobRepository.findAll(pageable);
        
        return jobs.map(this::toJobDTO);
    }

    @Override
    public List<String> getSearchSuggestions(String partialQuery) {
        // TODO: Implement search suggestions
        // This could be based on popular searches, job titles, company names, etc.
        return Arrays.asList(
            "Software Engineer",
            "Data Scientist", 
            "Product Manager",
            "Marketing Specialist",
            "Sales Representative"
        );
    }

    @Override
    public List<String> getTrendingKeywords() {
        // TODO: Implement trending keywords analysis
        // This could be based on recent searches, popular job titles, etc.
        return Arrays.asList(
            "Remote Work",
            "AI/ML",
            "Blockchain",
            "DevOps",
            "Cloud Computing"
        );
    }

    @Override
    @Transactional
    public void saveSearchHistory(UUID userId, String query, Map<String, Object> filters) {
        // TODO: Implement search history storage
        // This could be stored in a separate SearchHistory entity
    }

    @Override
    public List<Map<String, Object>> getSearchHistory(UUID userId) {
        // TODO: Implement search history retrieval
        return new ArrayList<>();
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
