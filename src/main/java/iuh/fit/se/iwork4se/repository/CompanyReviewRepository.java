package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.CompanyReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyReviewRepository extends JpaRepository<CompanyReview, UUID> {

    /**
     * Find review by company and job seeker profile
     */
    Optional<CompanyReview> findByCompanyCompanyIdAndJobSeekerProfileJobSeekerProfileId(
            UUID companyId, UUID jobSeekerProfileId);

    /**
     * Check if job seeker has already reviewed a company
     */
    boolean existsByCompanyCompanyIdAndJobSeekerProfileJobSeekerProfileId(
            UUID companyId, UUID jobSeekerProfileId);

    /**
     * Find all reviews for a specific company
     */
    Page<CompanyReview> findByCompanyCompanyIdOrderByCreatedAtDesc(UUID companyId, Pageable pageable);

    /**
     * Find all reviews by a specific job seeker
     */
    Page<CompanyReview> findByJobSeekerProfileJobSeekerProfileIdOrderByCreatedAtDesc(
            UUID jobSeekerProfileId, Pageable pageable);

    /**
     * Calculate average rating for a company
     */
    @Query("SELECT AVG(cr.rating) FROM CompanyReview cr WHERE cr.company.companyId = :companyId")
    Optional<Double> getAverageRatingByCompanyId(@Param("companyId") UUID companyId);

    /**
     * Count total reviews for a company
     */
    @Query("SELECT COUNT(cr) FROM CompanyReview cr WHERE cr.company.companyId = :companyId")
    Long countReviewsByCompanyId(@Param("companyId") UUID companyId);

    // Admin methods
    /**
     * Find reviews by status
     */
    Page<CompanyReview> findByStatus(String status, Pageable pageable);

    /**
     * Count reviews by status
     */
    Long countByStatus(String status);

    /**
     * Get average rating across all companies
     */
    @Query("SELECT AVG(cr.rating) FROM CompanyReview cr")
    Double getAverageRating();

    /**
     * Find reviews by company
     */
    Page<CompanyReview> findByCompany_CompanyId(UUID companyId, Pageable pageable);

    /**
     * Find low rating reviews
     */
    Page<CompanyReview> findByRatingLessThanEqual(Integer rating, Pageable pageable);
}
