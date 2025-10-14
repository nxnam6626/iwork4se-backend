package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CreateExperienceRequest;
import iuh.fit.se.iwork4se.dto.UpdateExperienceRequest;
import iuh.fit.se.iwork4se.dto.profile.ExperienceDTO;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/profile/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<List<ExperienceDTO>> listMine() {
        return ResponseEntity.ok(experienceService.listMyExperiences(SecurityUtil.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<ExperienceDTO> create(@Valid @RequestBody CreateExperienceRequest req) {
        ExperienceDTO created = experienceService.createMyExperience(
                SecurityUtil.currentUserId(),
                req.companyName(), req.title(), req.startDate(), req.endDate(), req.isCurrent(), req.description()
        );
        // Location: /api/job-seeker/profile/experiences/{id}
        return ResponseEntity
                .created(URI.create("/api/job-seeker/profile/experiences/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDTO> update(@PathVariable UUID id,
                                                @Valid @RequestBody UpdateExperienceRequest req) {
        ExperienceDTO updated = experienceService.updateMyExperience(
                SecurityUtil.currentUserId(),
                id, req.companyName(), req.title(), req.startDate(), req.endDate(), req.isCurrent(), req.description()
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        experienceService.deleteMyExperience(SecurityUtil.currentUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
