package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.ApplyJobRequest;
import iuh.fit.se.iwork4se.dto.profile.MyApplicationDTO;
import iuh.fit.se.iwork4se.service.JobSeekerApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/applications")
@RequiredArgsConstructor
public class JobSeekerApplicationController {

    private final JobSeekerApplicationService jobSeekerApplicationService;

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<MyApplicationDTO> apply(@PathVariable UUID jobId, @Valid @RequestBody ApplyJobRequest request) {
        return ResponseEntity.ok(jobSeekerApplicationService.applyToJob(jobId, request));
    }

    @GetMapping
    public ResponseEntity<Page<MyApplicationDTO>> getMyApplications(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobSeekerApplicationService.getMyApplications(page, size));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<MyApplicationDTO> getMyApplication(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(jobSeekerApplicationService.getMyApplication(applicationId));
    }
}
