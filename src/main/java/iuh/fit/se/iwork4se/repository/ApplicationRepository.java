package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Application;
import iuh.fit.se.iwork4se.model.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Page<Application> findByJob_Company_CompanyId(UUID companyId, Pageable pageable);
    Page<Application> findByJob_Company_CompanyIdAndStatus(UUID companyId, ApplicationStatus status, Pageable pageable);
    Page<Application> findByJob_JobId(UUID jobId, Pageable pageable);
    Page<Application> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId, Pageable pageable);
    boolean existsByJob_JobIdAndJobSeekerProfile_JobSeekerProfileId(UUID jobId, UUID jobSeekerProfileId);
    
    /**
     * Check if job seeker has applied to any job at a specific company
     */
    boolean existsByJob_Company_CompanyIdAndJobSeekerProfile_JobSeekerProfileId(UUID companyId, UUID jobSeekerProfileId);
}


