package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.CompanyReviewDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.CompanyReview;
import iuh.fit.se.iwork4se.repository.CompanyReviewRepository;
import iuh.fit.se.iwork4se.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReviewServiceImpl implements AdminReviewService {

    private final CompanyReviewRepository companyReviewRepository;

    @Override
    public Page<CompanyReviewDTO> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CompanyReview> reviews = companyReviewRepository.findAll(pageable);
        
        return reviews.map(this::toCompanyReviewDTO);
    }

    @Override
    public Page<CompanyReviewDTO> getReviewsByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CompanyReview> reviews = companyReviewRepository.findByStatus(status, pageable);
        
        return reviews.map(this::toCompanyReviewDTO);
    }

    @Override
    public CompanyReviewDTO getReviewDetail(UUID reviewId) {
        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        return toCompanyReviewDTO(review);
    }

    @Override
    @Transactional
    public CompanyReviewDTO approveReview(UUID reviewId) {
        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        review.setStatus("APPROVED");
        review = companyReviewRepository.save(review);
        
        return toCompanyReviewDTO(review);
    }

    @Override
    @Transactional
    public CompanyReviewDTO rejectReview(UUID reviewId, String reason) {
        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        review.setStatus("REJECTED");
        review.setAdminNotes(reason);
        review = companyReviewRepository.save(review);
        
        return toCompanyReviewDTO(review);
    }

    @Override
    @Transactional
    public void deleteReview(UUID reviewId) {
        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        companyReviewRepository.delete(review);
    }

    @Override
    @Transactional
    public CompanyReviewDTO toggleReviewVisibility(UUID reviewId) {
        CompanyReview review = companyReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        
        // Toggle visibility - giả sử có field visible trong CompanyReview model
        // review.setVisible(!review.isVisible());
        // review = companyReviewRepository.save(review);
        
        return toCompanyReviewDTO(review);
    }

    @Override
    public Map<String, Object> getReviewStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalReviews", companyReviewRepository.count());
        stats.put("approvedReviews", companyReviewRepository.countByStatus("APPROVED"));
        stats.put("pendingReviews", companyReviewRepository.countByStatus("PENDING"));
        stats.put("rejectedReviews", companyReviewRepository.countByStatus("REJECTED"));
        stats.put("averageRating", companyReviewRepository.getAverageRating());
        
        return stats;
    }

    @Override
    public Page<CompanyReviewDTO> getPendingReviews(int page, int size) {
        return getReviewsByStatus("PENDING", page, size);
    }

    @Override
    public Page<CompanyReviewDTO> getReviewsByCompany(UUID companyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CompanyReview> reviews = companyReviewRepository.findByCompany_CompanyId(companyId, pageable);
        
        return reviews.map(this::toCompanyReviewDTO);
    }

    @Override
    public Page<CompanyReviewDTO> getLowRatingReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "rating"));
        Page<CompanyReview> reviews = companyReviewRepository.findByRatingLessThanEqual(2, pageable);
        
        return reviews.map(this::toCompanyReviewDTO);
    }

    private CompanyReviewDTO toCompanyReviewDTO(CompanyReview review) {
        return CompanyReviewDTO.builder()
                .id(review.getReviewId())
                .companyId(review.getCompany().getCompanyId())
                .companyName(review.getCompany().getName())
                .jobSeekerProfileId(review.getJobSeekerProfile().getJobSeekerProfileId())
                .jobSeekerName(review.getJobSeekerProfile().getUser().getFullName())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .pros(review.getPros())
                .cons(review.getCons())
                .recommendToFriend(review.getRecommendToFriend())
                .status(review.getStatus())
                .adminNotes(review.getAdminNotes())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
