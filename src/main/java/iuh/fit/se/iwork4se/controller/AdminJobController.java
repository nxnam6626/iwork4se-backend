package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.PublicJobSearchRequest;
import iuh.fit.se.iwork4se.service.AdminJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
public class AdminJobController {

    private final AdminJobService adminJobService;

    /**
     * Lấy danh sách tất cả job với filter
     */
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<JobDTO>> getAllJobs(@Valid @RequestBody PublicJobSearchRequest request) {
        Page<JobDTO> result = adminJobService.getAllJobs(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy chi tiết một job
     */
    @GetMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> getJobDetail(@PathVariable UUID jobId) {
        JobDTO result = adminJobService.getJobDetail(jobId);
        return ResponseEntity.ok(result);
    }

    /**
     * Ẩn/hiện một job
     */
    @PutMapping("/{jobId}/toggle-visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> toggleJobVisibility(@PathVariable UUID jobId) {
        JobDTO result = adminJobService.toggleJobVisibility(jobId);
        return ResponseEntity.ok(result);
    }

    /**
     * Xóa job
     */
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID jobId) {
        adminJobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy thống kê job
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getJobStatistics() {
        Map<String, Object> stats = Map.of(
            "totalJobs", adminJobService.getJobCountByStatus("ALL"),
            "activeJobs", adminJobService.getJobCountByStatus("ACTIVE"),
            "expiredJobs", adminJobService.getJobCountByStatus("EXPIRED")
        );
        return ResponseEntity.ok(stats);
    }

    /**
     * Lấy danh sách job gần hết hạn
     */
    @GetMapping("/expiring")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<JobDTO>> getExpiringJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = adminJobService.getExpiringJobs(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách job của một công ty
     */
    @GetMapping("/by-company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<JobDTO>> getJobsByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = adminJobService.getJobsByCompany(companyId, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Tìm kiếm đơn giản cho admin
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<JobDTO>> simpleSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PublicJobSearchRequest request = new PublicJobSearchRequest();
        request.setKeyword(keyword);
        request.setCompanyName(companyName);
        request.setLocation(location);
        request.setPage(page);
        request.setSize(size);
        
        Page<JobDTO> result = adminJobService.getAllJobs(request);
        return ResponseEntity.ok(result);
    }
}
