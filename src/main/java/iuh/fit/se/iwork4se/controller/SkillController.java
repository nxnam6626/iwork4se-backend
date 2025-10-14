package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CreateSkillRequest;
import iuh.fit.se.iwork4se.dto.profile.SkillDTO;
import iuh.fit.se.iwork4se.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDTO>> listAll() {
        return ResponseEntity.ok(skillService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(skillService.get(id));
    }

    @GetMapping("/search")
    public ResponseEntity<SkillDTO> findByName(@RequestParam String name) {
        return ResponseEntity.of(skillService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<SkillDTO> create(@Valid @RequestBody CreateSkillRequest req) {
        return ResponseEntity.ok(skillService.create(req.name(), req.description()));
    }
}
