package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface JobSeekerFavoriteService {
    
    /**
     * Thêm job vào danh sách yêu thích
     */
    JobFavoriteDTO addToFavorites(UUID jobId, AddToFavoritesRequest request);
    
    /**
     * Xóa job khỏi danh sách yêu thích
     */
    void removeFromFavorites(UUID jobId);
    
    /**
     * Lấy danh sách job yêu thích của job seeker
     */
    Page<JobFavoriteSummaryDTO> getMyFavorites(int page, int size);
    
    /**
     * Lấy chi tiết một job favorite
     */
    JobFavoriteDTO getFavoriteDetail(UUID favoriteId);
    
    /**
     * Cập nhật ghi chú cho job favorite
     */
    JobFavoriteDTO updateFavoriteNotes(UUID favoriteId, UpdateFavoriteNotesRequest request);
    
    /**
     * Kiểm tra trạng thái yêu thích của một job
     */
    FavoriteStatusDTO getFavoriteStatus(UUID jobId);
    
    /**
     * Đếm số lượng job yêu thích
     */
    long getFavoriteCount();
}
