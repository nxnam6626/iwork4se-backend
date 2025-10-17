package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {
    Page<Job> findByCompany_CompanyIdOrderByCreatedAtDesc(UUID companyId, Pageable pageable);
    
    // Public search methods
    Page<Job> findByCompany_NameContainingIgnoreCase(String companyName, Pageable pageable);
    Page<Job> findByCompany_IndustryContainingIgnoreCase(String industry, Pageable pageable);
    Page<Job> findBySalaryMinGreaterThanEqual(Integer minSalary, Pageable pageable);
    
    // Admin methods
    Page<Job> findByExpireAtBeforeAndExpireAtAfter(LocalDate before, LocalDate after, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:employmentType IS NULL OR j.employmentType = :employmentType) AND " +
           "(:salaryMin IS NULL OR j.salaryMin >= :salaryMin) AND " +
           "(:salaryMax IS NULL OR j.salaryMax <= :salaryMax) AND " +
           "(:currency IS NULL OR j.currency = :currency) AND " +
           "(:remote IS NULL OR j.remote = :remote)")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                        @Param("location") String location,
                        @Param("employmentType") String employmentType,
                        @Param("salaryMin") Integer salaryMin,
                        @Param("salaryMax") Integer salaryMax,
                        @Param("currency") String currency,
                        @Param("remote") Boolean remote,
                        Pageable pageable);
}


