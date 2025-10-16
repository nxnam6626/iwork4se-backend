package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.AddSeekerSkillRequest;
import iuh.fit.se.iwork4se.dto.UpdateSeekerSkillLevelRequest;
import iuh.fit.se.iwork4se.dto.profile.SeekerSkillDTO;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.SeekerSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/profile/skills")
@RequiredArgsConstructor
public class SeekerSkillController {

    private final SeekerSkillService seekerSkillService;

    @GetMapping
    public ResponseEntity<List<SeekerSkillDTO>> listMine() {
        return ResponseEntity.ok(seekerSkillService.listMySeekerSkills(SecurityUtil.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<SeekerSkillDTO> add(@Valid @RequestBody AddSeekerSkillRequest req) {
        return ResponseEntity.ok(seekerSkillService.addMySkill(SecurityUtil.currentUserId(), req.skillName(), req.level()));
    }

    @PutMapping("/{seekerSkillId}")
    public ResponseEntity<SeekerSkillDTO> updateLevel(@PathVariable UUID seekerSkillId,
                                                      @Valid @RequestBody UpdateSeekerSkillLevelRequest req) {
        return ResponseEntity.ok(seekerSkillService.updateMySkillLevel(SecurityUtil.currentUserId(), seekerSkillId, req.level()));
    }

    @DeleteMapping("/{seekerSkillId}")
    public ResponseEntity<Void> remove(@PathVariable UUID seekerSkillId) {
        seekerSkillService.removeMySkill(SecurityUtil.currentUserId(), seekerSkillId);
        return ResponseEntity.noContent().build();
    }
}
