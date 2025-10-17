package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.service.JobSeekerFavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/favorites")
@RequiredArgsConstructor
@Slf4j
public class JobSeekerFavoriteController {

    private final JobSeekerFavoriteService jobSeekerFavoriteService;

    /**
     * Thêm job vào danh sách yêu thích
     */
    @PostMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<JobFavoriteDTO> addToFavorites(
            @PathVariable UUID jobId,
            @Valid @RequestBody AddToFavoritesRequest request) {
        
        JobFavoriteDTO result = jobSeekerFavoriteService.addToFavorites(jobId, request);
        return ResponseEntity.created(URI.create("/api/job-seeker/favorites/" + result.getId()))
                .body(result);
    }

    /**
     * Xóa job khỏi danh sách yêu thích
     */
    @DeleteMapping("/jobs/{jobId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable UUID jobId) {
        jobSeekerFavoriteService.removeFromFavorites(jobId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy danh sách job yêu thích
     */
    @GetMapping
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Page<JobFavoriteSummaryDTO>> getMyFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<JobFavoriteSummaryDTO> result = jobSeekerFavoriteService.getMyFavorites(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy chi tiết một job favorite
     */
    @GetMapping("/{favoriteId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<JobFavoriteDTO> getFavoriteDetail(@PathVariable UUID favoriteId) {
        JobFavoriteDTO result = jobSeekerFavoriteService.getFavoriteDetail(favoriteId);
        return ResponseEntity.ok(result);
    }

    /**
     * Cập nhật ghi chú cho job favorite
     */
    @PutMapping("/{favoriteId}/notes")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<JobFavoriteDTO> updateFavoriteNotes(
            @PathVariable UUID favoriteId,
            @Valid @RequestBody UpdateFavoriteNotesRequest request) {
        
        JobFavoriteDTO result = jobSeekerFavoriteService.updateFavoriteNotes(favoriteId, request);
        return ResponseEntity.ok(result);
    }

    /**
     * Kiểm tra trạng thái yêu thích của một job
     */
    @GetMapping("/status/jobs/{jobId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<FavoriteStatusDTO> getFavoriteStatus(@PathVariable UUID jobId) {
        FavoriteStatusDTO result = jobSeekerFavoriteService.getFavoriteStatus(jobId);
        return ResponseEntity.ok(result);
    }

    /**
     * Đếm số lượng job yêu thích
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Long> getFavoriteCount() {
        long count = jobSeekerFavoriteService.getFavoriteCount();
        return ResponseEntity.ok(count);
    }
}
