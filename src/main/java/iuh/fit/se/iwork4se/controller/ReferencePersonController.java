package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CreateReferenceRequest;
import iuh.fit.se.iwork4se.dto.UpdateReferenceRequest;
import iuh.fit.se.iwork4se.dto.profile.ReferencePersonDTO;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.ReferencePersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/profile/references")
@RequiredArgsConstructor
public class ReferencePersonController {

    private final ReferencePersonService referenceService;

    @GetMapping
    public ResponseEntity<List<ReferencePersonDTO>> listMine() {
        return ResponseEntity.ok(referenceService.listMyReferences(SecurityUtil.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<ReferencePersonDTO> create(@Valid @RequestBody CreateReferenceRequest req) {
        ReferencePersonDTO created = referenceService.createMyReference(
                SecurityUtil.currentUserId(), req.name(), req.company(), req.email(), req.relation()
        );
        return ResponseEntity.created(URI.create("/api/job-seeker/profile/references/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReferencePersonDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateReferenceRequest req) {
        return ResponseEntity.ok(referenceService.updateMyReference(
                SecurityUtil.currentUserId(), id, req.name(), req.company(), req.email(), req.relation()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        referenceService.deleteMyReference(SecurityUtil.currentUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
