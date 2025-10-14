package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.EducationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EducationService {
    List<EducationDTO> listMyEducations(UUID currentUserId);
    EducationDTO createMyEducation(UUID currentUserId, String school, String degree, String major,
                                   LocalDate startYear, LocalDate endYear, String description);
    EducationDTO updateMyEducation(UUID currentUserId, UUID educationId, String school, String degree, String major,
                                   LocalDate startYear, LocalDate endYear, String description);
    void deleteMyEducation(UUID currentUserId, UUID educationId);
}
