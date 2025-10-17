package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.PublicJobSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AdminJobService {
    
    /**
     * Lấy danh sách tất cả job trong hệ thống với filter
     */
    Page<JobDTO> getAllJobs(PublicJobSearchRequest request);
    
    /**
     * Lấy chi tiết một job (admin có thể xem tất cả)
     */
    JobDTO getJobDetail(UUID jobId);
    
    /**
     * Ẩn/hiện một job
     */
    JobDTO toggleJobVisibility(UUID jobId);
    
    /**
     * Xóa job (hard delete)
     */
    void deleteJob(UUID jobId);
    
    /**
     * Lấy thống kê job theo trạng thái
     */
    long getJobCountByStatus(String status);
    
    /**
     * Lấy danh sách job gần hết hạn
     */
    Page<JobDTO> getExpiringJobs(int page, int size);
    
    /**
     * Lấy danh sách job của một công ty cụ thể
     */
    Page<JobDTO> getJobsByCompany(UUID companyId, int page, int size);
}
