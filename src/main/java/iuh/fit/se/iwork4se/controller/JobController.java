package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/{jobId}")
    public ResponseEntity<JobDTO> getJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(jobService.getJobDetail(jobId));
    }
}


