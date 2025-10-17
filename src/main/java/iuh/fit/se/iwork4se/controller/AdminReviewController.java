package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CompanyReviewDTO;
import iuh.fit.se.iwork4se.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    /**
     * Lấy danh sách tất cả reviews
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CompanyReviewDTO>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<CompanyReviewDTO> result = adminReviewService.getAllReviews(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách reviews theo trạng thái
     */
    @GetMapping("/by-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CompanyReviewDTO>> getReviewsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<CompanyReviewDTO> result = adminReviewService.getReviewsByStatus(status, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy chi tiết một review
     */
    @GetMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyReviewDTO> getReviewDetail(@PathVariable UUID reviewId) {
        CompanyReviewDTO result = adminReviewService.getReviewDetail(reviewId);
        return ResponseEntity.ok(result);
    }

    /**
     * Duyệt review
     */
    @PutMapping("/{reviewId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyReviewDTO> approveReview(@PathVariable UUID reviewId) {
        CompanyReviewDTO result = adminReviewService.approveReview(reviewId);
        return ResponseEntity.ok(result);
    }

    /**
     * Từ chối review
     */
    @PutMapping("/{reviewId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyReviewDTO> rejectReview(
            @PathVariable UUID reviewId,
            @RequestParam(required = false) String reason) {
        
        CompanyReviewDTO result = adminReviewService.rejectReview(reviewId, reason);
        return ResponseEntity.ok(result);
    }

    /**
     * Xóa review
     */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        adminReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ẩn/hiện review
     */
    @PutMapping("/{reviewId}/toggle-visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyReviewDTO> toggleReviewVisibility(@PathVariable UUID reviewId) {
        CompanyReviewDTO result = adminReviewService.toggleReviewVisibility(reviewId);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê reviews
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getReviewStatistics() {
        Map<String, Object> result = adminReviewService.getReviewStatistics();
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách reviews chờ duyệt
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CompanyReviewDTO>> getPendingReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<CompanyReviewDTO> result = adminReviewService.getPendingReviews(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách reviews của một công ty
     */
    @GetMapping("/by-company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CompanyReviewDTO>> getReviewsByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<CompanyReviewDTO> result = adminReviewService.getReviewsByCompany(companyId, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách reviews có rating thấp
     */
    @GetMapping("/low-rating")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CompanyReviewDTO>> getLowRatingReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<CompanyReviewDTO> result = adminReviewService.getLowRatingReviews(page, size);
        return ResponseEntity.ok(result);
    }
}
