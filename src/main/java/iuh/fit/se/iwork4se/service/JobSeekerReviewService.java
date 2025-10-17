package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobSeekerReviewService {

    /**
     * Create a new company review
     */
    CompanyReviewDTO createReview(UUID jobSeekerProfileId, CreateCompanyReviewRequest request);

    /**
     * Get all reviews written by a job seeker
     */
    Page<CompanyReviewDTO> getMyReviews(UUID jobSeekerProfileId, Pageable pageable);

    /**
     * Update an existing review
     */
    CompanyReviewDTO updateReview(UUID reviewId, UUID jobSeekerProfileId, UpdateCompanyReviewRequest request);

    /**
     * Delete a review
     */
    void deleteReview(UUID reviewId, UUID jobSeekerProfileId);

    /**
     * Get all reviews for a specific company (public endpoint)
     */
    Page<CompanyReviewDTO> getCompanyReviews(UUID companyId, Pageable pageable);

    /**
     * Get company rating summary (public endpoint)
     */
    CompanyRatingSummaryDTO getCompanyRatingSummary(UUID companyId);
}
