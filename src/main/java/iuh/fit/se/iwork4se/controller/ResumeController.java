package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CreateResumeRequest;
import iuh.fit.se.iwork4se.dto.RenameResumeRequest;
import iuh.fit.se.iwork4se.dto.SetCurrentFileRequest;
import iuh.fit.se.iwork4se.dto.profile.ResumeDTO;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-seeker/profile/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public ResponseEntity<List<ResumeDTO>> listMine() {
        return ResponseEntity.ok(resumeService.listMyResumes(SecurityUtil.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<ResumeDTO> create(@Valid @RequestBody CreateResumeRequest req) {
        ResumeDTO created = resumeService.createMyResume(SecurityUtil.currentUserId(), req.title());
        return ResponseEntity.created(URI.create("/api/job-seeker/profile/resumes/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(resumeService.getMyResume(SecurityUtil.currentUserId(), id));
    }

    @PutMapping("/{id}/current-file")
    public ResponseEntity<ResumeDTO> setCurrentFile(@PathVariable UUID id, @Valid @RequestBody SetCurrentFileRequest req) {
        return ResponseEntity.ok(resumeService.setMyCurrentFile(SecurityUtil.currentUserId(), id, req.cvFileId()));
    }

    @PutMapping("/{id}/rename")
    public ResponseEntity<ResumeDTO> rename(@PathVariable UUID id, @Valid @RequestBody RenameResumeRequest req) {
        return ResponseEntity.ok(resumeService.renameMyResume(SecurityUtil.currentUserId(), id, req.newTitle()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resumeService.deleteMyResume(SecurityUtil.currentUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
