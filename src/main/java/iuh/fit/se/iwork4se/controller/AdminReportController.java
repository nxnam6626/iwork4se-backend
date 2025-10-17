package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    /**
     * Lấy thống kê tổng quan hệ thống
     */
    @GetMapping("/system-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemStatisticsDTO> getSystemStatistics() {
        SystemStatisticsDTO result = adminReportService.getSystemStatistics();
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê về job
     */
    @GetMapping("/job-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobStatisticsDTO> getJobStatistics() {
        JobStatisticsDTO result = adminReportService.getJobStatistics();
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê về user
     */
    @GetMapping("/user-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserStatisticsDTO> getUserStatistics() {
        UserStatisticsDTO result = adminReportService.getUserStatistics();
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê theo khoảng thời gian
     */
    @GetMapping("/statistics-by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemStatisticsDTO> getStatisticsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        SystemStatisticsDTO result = adminReportService.getStatisticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy top công ty có nhiều job nhất
     */
    @GetMapping("/top-companies-by-jobs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTopCompaniesByJobCount(
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> result = adminReportService.getTopCompaniesByJobCount(limit);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy top job được apply nhiều nhất
     */
    @GetMapping("/top-jobs-by-applications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTopJobsByApplicationCount(
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> result = adminReportService.getTopJobsByApplicationCount(limit);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê xu hướng ứng tuyển
     */
    @GetMapping("/application-trends")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getApplicationTrends() {
        Map<String, Object> result = adminReportService.getApplicationTrends();
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy thống kê đánh giá công ty
     */
    @GetMapping("/company-review-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCompanyReviewStatistics() {
        Map<String, Object> result = adminReportService.getCompanyReviewStatistics();
        return ResponseEntity.ok(result);
    }

    /**
     * Dashboard tổng hợp
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = Map.of(
            "systemStats", adminReportService.getSystemStatistics(),
            "jobStats", adminReportService.getJobStatistics(),
            "userStats", adminReportService.getUserStatistics(),
            "reviewStats", adminReportService.getCompanyReviewStatistics()
        );
        return ResponseEntity.ok(dashboard);
    }
}
