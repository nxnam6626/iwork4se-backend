package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.JobSearchRequest;
import iuh.fit.se.iwork4se.service.JobSeekerJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/jobs")
@RequiredArgsConstructor
public class JobSeekerJobController {

    private final JobSeekerJobService jobSeekerJobService;

    @PostMapping("/search")
    public ResponseEntity<Page<JobDTO>> search(@Valid @RequestBody JobSearchRequest request) {
        return ResponseEntity.ok(jobSeekerJobService.searchJobs(request));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDTO> getJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(jobSeekerJobService.getJobDetail(jobId));
    }
}
