package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.*;

import java.time.LocalDate;
import java.util.Map;

public interface AdminReportService {
    
    /**
     * Lấy thống kê tổng quan hệ thống
     */
    SystemStatisticsDTO getSystemStatistics();
    
    /**
     * Lấy thống kê về job
     */
    JobStatisticsDTO getJobStatistics();
    
    /**
     * Lấy thống kê về user
     */
    UserStatisticsDTO getUserStatistics();
    
    /**
     * Lấy thống kê theo khoảng thời gian
     */
    SystemStatisticsDTO getStatisticsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Lấy top công ty có nhiều job nhất
     */
    Map<String, Object> getTopCompaniesByJobCount(int limit);
    
    /**
     * Lấy top job được apply nhiều nhất
     */
    Map<String, Object> getTopJobsByApplicationCount(int limit);
    
    /**
     * Lấy thống kê ứng tuyển theo thời gian
     */
    Map<String, Object> getApplicationTrends();
    
    /**
     * Lấy thống kê đánh giá công ty
     */
    Map<String, Object> getCompanyReviewStatistics();
}
