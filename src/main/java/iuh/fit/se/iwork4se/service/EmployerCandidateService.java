package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.CandidateDetailDTO;

import java.util.UUID;

public interface EmployerCandidateService {
    CandidateDetailDTO getCandidateDetail(UUID jobSeekerProfileId);
}


