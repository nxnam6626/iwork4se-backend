package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.profile.CandidateDetailDTO;
import iuh.fit.se.iwork4se.service.EmployerCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employer/candidates")
@RequiredArgsConstructor
public class EmployerCandidateController {

    private final EmployerCandidateService employerCandidateService;

    @GetMapping("/{jobSeekerProfileId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<CandidateDetailDTO> getDetail(@PathVariable UUID jobSeekerProfileId) {
        return ResponseEntity.ok(employerCandidateService.getCandidateDetail(jobSeekerProfileId));
    }
}


