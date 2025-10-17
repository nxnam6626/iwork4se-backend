package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.JobSeekerReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/reviews")
@RequiredArgsConstructor
@Slf4j
public class JobSeekerReviewController {

    private final JobSeekerReviewService jobSeekerReviewService;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    private UUID getCurrentJobSeekerProfileId() {
        UUID userId = SecurityUtil.currentUserId();
        JobSeekerProfile profile = jobSeekerProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        return profile.getJobSeekerProfileId();
    }

    @PostMapping
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<CompanyReviewDTO> createReview(@Valid @RequestBody CreateCompanyReviewRequest request) {
        log.info("Creating review for company: {}", request.getCompanyId());
        
        UUID jobSeekerProfileId = getCurrentJobSeekerProfileId();
        CompanyReviewDTO review = jobSeekerReviewService.createReview(jobSeekerProfileId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Page<CompanyReviewDTO>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Getting reviews for current job seeker");
        
        UUID jobSeekerProfileId = getCurrentJobSeekerProfileId();
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CompanyReviewDTO> reviews = jobSeekerReviewService.getMyReviews(jobSeekerProfileId, pageable);
        
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<CompanyReviewDTO> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody UpdateCompanyReviewRequest request) {
        
        log.info("Updating review: {}", reviewId);
        
        UUID jobSeekerProfileId = getCurrentJobSeekerProfileId();
        CompanyReviewDTO review = jobSeekerReviewService.updateReview(reviewId, jobSeekerProfileId, request);
        
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        log.info("Deleting review: {}", reviewId);
        
        UUID jobSeekerProfileId = getCurrentJobSeekerProfileId();
        jobSeekerReviewService.deleteReview(reviewId, jobSeekerProfileId);
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<Page<CompanyReviewDTO>> getCompanyReviews(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Getting reviews for company: {}", companyId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CompanyReviewDTO> reviews = jobSeekerReviewService.getCompanyReviews(companyId, pageable);
        
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/companies/{companyId}/rating")
    public ResponseEntity<CompanyRatingSummaryDTO> getCompanyRatingSummary(@PathVariable UUID companyId) {
        log.info("Getting rating summary for company: {}", companyId);
        
        CompanyRatingSummaryDTO summary = jobSeekerReviewService.getCompanyRatingSummary(companyId);
        
        return ResponseEntity.ok(summary);
    }
}
