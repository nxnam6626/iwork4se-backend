package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.JobDTO;
import iuh.fit.se.iwork4se.dto.profile.EmployerJobRequest;
import iuh.fit.se.iwork4se.service.EmployerJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employer/jobs")
@RequiredArgsConstructor
public class EmployerJobController {

    private final EmployerJobService employerJobService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDTO> create(@Valid @RequestBody EmployerJobRequest request) {
        return ResponseEntity.ok(employerJobService.createJob(request));
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDTO> update(@PathVariable UUID jobId, @Valid @RequestBody EmployerJobRequest request) {
        return ResponseEntity.ok(employerJobService.updateJob(jobId, request));
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> delete(@PathVariable UUID jobId) {
        employerJobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<JobDTO>> list(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employerJobService.listMyCompanyJobs(page, size));
    }
}


