package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.UpdateProfileRequest;
import iuh.fit.se.iwork4se.dto.profile.JobSeekerProfileDTO;
import iuh.fit.se.iwork4se.service.JobSeekerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-seeker/profile")
@RequiredArgsConstructor
@Validated
public class JobSeekerProfileController {

    private final JobSeekerProfileService profileService;

    @PostMapping("/init")
    public ResponseEntity<JobSeekerProfileDTO> initMyProfile() {
        return ResponseEntity.ok(profileService.initMyProfileIfAbsent(SecurityUtil.currentUserId()));
    }

    @GetMapping("/me")
    public ResponseEntity<JobSeekerProfileDTO> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile(SecurityUtil.currentUserId()));
    }

    @PutMapping
    public ResponseEntity<JobSeekerProfileDTO> updateMyProfile(@Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(
                profileService.updateMyProfile(
                        SecurityUtil.currentUserId(),
                        req.headline(), req.summary(), req.location(), req.yearsExp()
                )
        );
    }
}
