package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.profile.ApplicantSummaryDTO;
import iuh.fit.se.iwork4se.dto.profile.ApplicationDetailDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateApplicationStatusRequest;
import iuh.fit.se.iwork4se.model.ApplicationStatus;
import iuh.fit.se.iwork4se.service.EmployerApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employer/applications")
@RequiredArgsConstructor
public class EmployerApplicationController {

    private final EmployerApplicationService employerApplicationService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<ApplicantSummaryDTO>> list(@RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) ApplicationStatus status,
                                                          @RequestParam(required = false) UUID jobId) {
        return ResponseEntity.ok(employerApplicationService.listApplicants(page, size, status, jobId));
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApplicationDetailDTO> get(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(employerApplicationService.getApplication(applicationId));
    }

    @PostMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApplicationDetailDTO> updateStatus(@PathVariable UUID applicationId,
                                                             @Valid @RequestBody UpdateApplicationStatusRequest request) {
        return ResponseEntity.ok(employerApplicationService.updateStatus(applicationId, request));
    }
}


