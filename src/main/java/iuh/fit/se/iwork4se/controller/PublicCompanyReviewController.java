package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CompanyRatingSummaryDTO;
import iuh.fit.se.iwork4se.dto.CompanyReviewDTO;
import iuh.fit.se.iwork4se.service.JobSeekerReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/companies")
@RequiredArgsConstructor
@Slf4j
public class PublicCompanyReviewController {

    private final JobSeekerReviewService jobSeekerReviewService;

    @GetMapping("/{companyId}/reviews")
    public ResponseEntity<Page<CompanyReviewDTO>> getCompanyReviews(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.info("Getting public reviews for company: {}", companyId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CompanyReviewDTO> reviews = jobSeekerReviewService.getCompanyReviews(companyId, pageable);
        
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{companyId}/rating")
    public ResponseEntity<CompanyRatingSummaryDTO> getCompanyRatingSummary(@PathVariable UUID companyId) {
        log.info("Getting public rating summary for company: {}", companyId);
        
        CompanyRatingSummaryDTO summary = jobSeekerReviewService.getCompanyRatingSummary(companyId);
        
        return ResponseEntity.ok(summary);
    }
}
