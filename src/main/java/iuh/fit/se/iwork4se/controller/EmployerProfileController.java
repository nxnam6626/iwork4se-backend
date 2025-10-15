package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.profile.EmployerProfileDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateEmployerProfileRequest;
import iuh.fit.se.iwork4se.service.EmployerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> getMyProfile() {
        return ResponseEntity.ok(employerProfileService.getMyEmployerProfile());
    }

    @PutMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<EmployerProfileDTO> updateMyProfile(@Valid @RequestBody UpdateEmployerProfileRequest request) {
        return ResponseEntity.ok(employerProfileService.upsertMyEmployerProfile(request));
    }
}


