package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.CompanyReviewDTO;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.UUID;

public interface AdminReviewService {
    
    /**
     * Lấy danh sách tất cả reviews với filter
     */
    Page<CompanyReviewDTO> getAllReviews(int page, int size);
    
    /**
     * Lấy danh sách reviews theo trạng thái
     */
    Page<CompanyReviewDTO> getReviewsByStatus(String status, int page, int size);
    
    /**
     * Lấy chi tiết một review
     */
    CompanyReviewDTO getReviewDetail(UUID reviewId);
    
    /**
     * Duyệt review (approve)
     */
    CompanyReviewDTO approveReview(UUID reviewId);
    
    /**
     * Từ chối review (reject)
     */
    CompanyReviewDTO rejectReview(UUID reviewId, String reason);
    
    /**
     * Xóa review (hard delete)
     */
    void deleteReview(UUID reviewId);
    
    /**
     * Ẩn/hiện review
     */
    CompanyReviewDTO toggleReviewVisibility(UUID reviewId);
    
    /**
     * Lấy thống kê reviews
     */
    Map<String, Object> getReviewStatistics();
    
    /**
     * Lấy danh sách reviews chờ duyệt
     */
    Page<CompanyReviewDTO> getPendingReviews(int page, int size);
    
    /**
     * Lấy danh sách reviews của một công ty
     */
    Page<CompanyReviewDTO> getReviewsByCompany(UUID companyId, int page, int size);
    
    /**
     * Lấy danh sách reviews có rating thấp (cần chú ý)
     */
    Page<CompanyReviewDTO> getLowRatingReviews(int page, int size);
}
