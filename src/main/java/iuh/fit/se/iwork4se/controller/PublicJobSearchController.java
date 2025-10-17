package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.PublicJobSearchRequest;
import iuh.fit.se.iwork4se.service.PublicJobSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/jobs")
@RequiredArgsConstructor
@Slf4j
public class PublicJobSearchController {

    private final PublicJobSearchService publicJobSearchService;

    /**
     * Tìm kiếm job cho khách vãng lai
     */
    @PostMapping("/search")
    public ResponseEntity<Page<JobDTO>> searchJobs(@Valid @RequestBody PublicJobSearchRequest request) {
        Page<JobDTO> result = publicJobSearchService.searchJobs(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách job mới nhất
     */
    @GetMapping("/latest")
    public ResponseEntity<Page<JobDTO>> getLatestJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = publicJobSearchService.getLatestJobs(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách job theo công ty
     */
    @GetMapping("/by-company")
    public ResponseEntity<Page<JobDTO>> getJobsByCompany(
            @RequestParam String companyName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = publicJobSearchService.getJobsByCompany(companyName, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách job theo ngành nghề
     */
    @GetMapping("/by-industry")
    public ResponseEntity<Page<JobDTO>> getJobsByIndustry(
            @RequestParam String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = publicJobSearchService.getJobsByIndustry(industry, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách job có mức lương cao
     */
    @GetMapping("/high-salary")
    public ResponseEntity<Page<JobDTO>> getHighSalaryJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = publicJobSearchService.getHighSalaryJobs(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Tìm kiếm đơn giản bằng keyword
     */
    @GetMapping("/search")
    public ResponseEntity<Page<JobDTO>> simpleSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PublicJobSearchRequest request = new PublicJobSearchRequest();
        request.setKeyword(keyword);
        request.setLocation(location);
        request.setEmploymentType(employmentType);
        request.setPage(page);
        request.setSize(size);
        
        Page<JobDTO> result = publicJobSearchService.searchJobs(request);
        return ResponseEntity.ok(result);
    }
}
