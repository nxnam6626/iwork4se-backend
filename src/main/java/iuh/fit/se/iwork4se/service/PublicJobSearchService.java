package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.PublicJobSearchRequest;
import org.springframework.data.domain.Page;

public interface PublicJobSearchService {
    
    /**
     * Tìm kiếm job cho khách vãng lai (không cần đăng nhập)
     */
    Page<JobDTO> searchJobs(PublicJobSearchRequest request);
    
    /**
     * Lấy danh sách job mới nhất
     */
    Page<JobDTO> getLatestJobs(int page, int size);
    
    /**
     * Lấy danh sách job theo công ty
     */
    Page<JobDTO> getJobsByCompany(String companyName, int page, int size);
    
    /**
     * Lấy danh sách job theo industry
     */
    Page<JobDTO> getJobsByIndustry(String industry, int page, int size);
    
    /**
     * Lấy danh sách job có mức lương cao
     */
    Page<JobDTO> getHighSalaryJobs(int page, int size);
}
