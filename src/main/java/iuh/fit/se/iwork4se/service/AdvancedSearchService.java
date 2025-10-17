package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AdvancedSearchService {
    
    /**
     * Tìm kiếm job với full-text search
     */
    Page<JobDTO> fullTextSearch(String query, int page, int size);
    
    /**
     * Tìm kiếm job theo skills
     */
    Page<JobDTO> searchJobsBySkills(List<String> skills, int page, int size);
    
    /**
     * Tìm kiếm job theo location với radius
     */
    Page<JobDTO> searchJobsByLocation(String location, double radiusKm, int page, int size);
    
    /**
     * Tìm kiếm job với salary range và benefits
     */
    Page<JobDTO> searchJobsBySalaryAndBenefits(Integer minSalary, Integer maxSalary, 
                                               List<String> benefits, int page, int size);
    
    /**
     * Tìm kiếm job theo company culture
     */
    Page<JobDTO> searchJobsByCompanyCulture(List<String> cultureKeywords, int page, int size);
    
    /**
     * Tìm kiếm job theo work arrangement
     */
    Page<JobDTO> searchJobsByWorkArrangement(Boolean remote, Boolean hybrid, 
                                             Boolean onSite, int page, int size);
    
    /**
     * Tìm kiếm job với AI-powered matching
     */
    Page<JobDTO> getRecommendedJobs(UUID jobSeekerProfileId, int page, int size);
    
    /**
     * Lấy search suggestions
     */
    List<String> getSearchSuggestions(String partialQuery);
    
    /**
     * Lấy trending job keywords
     */
    List<String> getTrendingKeywords();
    
    /**
     * Lưu search history
     */
    void saveSearchHistory(UUID userId, String query, Map<String, Object> filters);
    
    /**
     * Lấy search history của user
     */
    List<Map<String, Object>> getSearchHistory(UUID userId);
}
