package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.*;
import iuh.fit.se.iwork4se.repository.ApplicationRepository;
import iuh.fit.se.iwork4se.repository.CompanyRepository;
import iuh.fit.se.iwork4se.repository.CompanyReviewRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.service.JobSeekerReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobSeekerReviewServiceImpl implements JobSeekerReviewService {

    private final CompanyReviewRepository companyReviewRepository;
    private final CompanyRepository companyRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public CompanyReviewDTO createReview(UUID jobSeekerProfileId, CreateCompanyReviewRequest request) {
        log.info("Creating review for company {} by job seeker {}", request.getCompanyId(), jobSeekerProfileId);

        // Validate that company exists
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));

        // Validate that job seeker profile exists
        JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(jobSeekerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found with id: " + jobSeekerProfileId));

        // Check if job seeker has applied to this company
        boolean hasApplied = applicationRepository.existsByJob_Company_CompanyIdAndJobSeekerProfile_JobSeekerProfileId(
                request.getCompanyId(), jobSeekerProfileId);
        
        if (!hasApplied) {
            throw new ForbiddenException("You can only review companies you have applied to");
        }

        // Check if job seeker has already reviewed this company
        boolean alreadyReviewed = companyReviewRepository.existsByCompanyCompanyIdAndJobSeekerProfileJobSeekerProfileId(
                request.getCompanyId(), jobSeekerProfileId);
        
        if (alreadyReviewed) {
            throw new ForbiddenException("You have already reviewed this company");
        }

        // Create the review
        CompanyReview review = CompanyReview.builder()
                .company(company)
                .jobSeekerProfile(jobSeekerProfile)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        CompanyReview savedReview = companyReviewRepository.save(review);
        log.info("Successfully created review with id: {}", savedReview.getReviewId());

        return convertToDTO(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyReviewDTO> getMyReviews(UUID jobSeekerProfileId, Pageable pageable) {
        log.info("Getting reviews for job seeker profile: {}", jobSeekerProfileId);
        
        return companyReviewRepository.findByJobSeekerProfileJobSeekerProfileIdOrderByCreatedAtDesc(jobSeekerProfileId, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public CompanyReviewDTO updateReview(UUID reviewId, UUID jobSeekerProfileId, UpdateCompanyReviewRequest request) {
        log.info("Updating review {} by job seeker {}", reviewId, jobSeekerProfileId);

        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Check if the review belongs to the job seeker
        if (!review.getJobSeekerProfile().getJobSeekerProfileId().equals(jobSeekerProfileId)) {
            throw new ForbiddenException("You can only update your own reviews");
        }

        // Update the review
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        CompanyReview updatedReview = companyReviewRepository.save(review);
        log.info("Successfully updated review with id: {}", updatedReview.getReviewId());

        return convertToDTO(updatedReview);
    }

    @Override
    public void deleteReview(UUID reviewId, UUID jobSeekerProfileId) {
        log.info("Deleting review {} by job seeker {}", reviewId, jobSeekerProfileId);

        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        // Check if the review belongs to the job seeker
        if (!review.getJobSeekerProfile().getJobSeekerProfileId().equals(jobSeekerProfileId)) {
            throw new ForbiddenException("You can only delete your own reviews");
        }

        companyReviewRepository.delete(review);
        log.info("Successfully deleted review with id: {}", reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyReviewDTO> getCompanyReviews(UUID companyId, Pageable pageable) {
        log.info("Getting reviews for company: {}", companyId);

        // Validate that company exists
        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        return companyReviewRepository.findByCompanyCompanyIdOrderByCreatedAtDesc(companyId, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyRatingSummaryDTO getCompanyRatingSummary(UUID companyId) {
        log.info("Getting rating summary for company: {}", companyId);

        // Validate that company exists
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        Double averageRating = companyReviewRepository.getAverageRatingByCompanyId(companyId).orElse(0.0);
        Long totalReviews = companyReviewRepository.countReviewsByCompanyId(companyId);

        return CompanyRatingSummaryDTO.builder()
                .companyId(companyId)
                .companyName(company.getName())
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .build();
    }

    private CompanyReviewDTO convertToDTO(CompanyReview review) {
        return CompanyReviewDTO.builder()
                .reviewId(review.getReviewId())
                .companyId(review.getCompany().getCompanyId())
                .companyName(review.getCompany().getName())
                .jobSeekerProfileId(review.getJobSeekerProfile().getJobSeekerProfileId())
                .jobSeekerName(review.getJobSeekerProfile().getUser().getFullName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
