package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.CandidateSearchRequest;
import iuh.fit.se.iwork4se.dto.profile.CandidateSummaryDTO;
import org.springframework.data.domain.Page;

public interface CandidateSearchService {
    Page<CandidateSummaryDTO> searchCandidates(CandidateSearchRequest request);
}


