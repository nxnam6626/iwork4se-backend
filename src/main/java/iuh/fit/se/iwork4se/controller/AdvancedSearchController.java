package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.service.AdvancedSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/advanced-search")
@RequiredArgsConstructor
public class AdvancedSearchController {

    private final AdvancedSearchService advancedSearchService;

    /**
     * Full-text search
     */
    @GetMapping("/full-text")
    public ResponseEntity<Page<JobDTO>> fullTextSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.fullTextSearch(query, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Search by skills
     */
    @PostMapping("/by-skills")
    public ResponseEntity<Page<JobDTO>> searchJobsBySkills(
            @RequestBody List<String> skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.searchJobsBySkills(skills, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Search by location with radius
     */
    @GetMapping("/by-location")
    public ResponseEntity<Page<JobDTO>> searchJobsByLocation(
            @RequestParam String location,
            @RequestParam(defaultValue = "10.0") double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.searchJobsByLocation(location, radiusKm, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Search by salary and benefits
     */
    @PostMapping("/by-salary-benefits")
    public ResponseEntity<Page<JobDTO>> searchJobsBySalaryAndBenefits(
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            @RequestBody(required = false) List<String> benefits,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.searchJobsBySalaryAndBenefits(
                minSalary, maxSalary, benefits, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Search by company culture
     */
    @PostMapping("/by-company-culture")
    public ResponseEntity<Page<JobDTO>> searchJobsByCompanyCulture(
            @RequestBody List<String> cultureKeywords,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.searchJobsByCompanyCulture(cultureKeywords, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Search by work arrangement
     */
    @GetMapping("/by-work-arrangement")
    public ResponseEntity<Page<JobDTO>> searchJobsByWorkArrangement(
            @RequestParam(required = false) Boolean remote,
            @RequestParam(required = false) Boolean hybrid,
            @RequestParam(required = false) Boolean onSite,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobDTO> result = advancedSearchService.searchJobsByWorkArrangement(remote, hybrid, onSite, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Get AI-powered job recommendations
     */
    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Page<JobDTO>> getRecommendedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // TODO: Get jobSeekerProfileId from current user context
        UUID jobSeekerProfileId = UUID.randomUUID(); // Placeholder
        Page<JobDTO> result = advancedSearchService.getRecommendedJobs(jobSeekerProfileId, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Get search suggestions
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSearchSuggestions(@RequestParam String query) {
        List<String> result = advancedSearchService.getSearchSuggestions(query);
        return ResponseEntity.ok(result);
    }

    /**
     * Get trending keywords
     */
    @GetMapping("/trending-keywords")
    public ResponseEntity<List<String>> getTrendingKeywords() {
        List<String> result = advancedSearchService.getTrendingKeywords();
        return ResponseEntity.ok(result);
    }

    /**
     * Save search history (for authenticated users)
     */
    @PostMapping("/save-history")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Void> saveSearchHistory(
            @RequestParam String query,
            @RequestBody(required = false) Map<String, Object> filters) {
        
        // TODO: Get userId from current user context
        UUID userId = UUID.randomUUID(); // Placeholder
        advancedSearchService.saveSearchHistory(userId, query, filters);
        return ResponseEntity.ok().build();
    }

    /**
     * Get search history (for authenticated users)
     */
    @GetMapping("/search-history")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<List<Map<String, Object>>> getSearchHistory() {
        // TODO: Get userId from current user context
        UUID userId = UUID.randomUUID(); // Placeholder
        List<Map<String, Object>> result = advancedSearchService.getSearchHistory(userId);
        return ResponseEntity.ok(result);
    }
}
