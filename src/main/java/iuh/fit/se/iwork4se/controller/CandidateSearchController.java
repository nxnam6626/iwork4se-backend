package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.profile.CandidateSearchRequest;
import iuh.fit.se.iwork4se.dto.profile.CandidateSummaryDTO;
import iuh.fit.se.iwork4se.service.CandidateSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/candidates")
@RequiredArgsConstructor
public class CandidateSearchController {

    private final CandidateSearchService candidateSearchService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<CandidateSummaryDTO>> search(@Valid @RequestBody CandidateSearchRequest request) {
        return ResponseEntity.ok(candidateSearchService.searchCandidates(request));
    }
}


