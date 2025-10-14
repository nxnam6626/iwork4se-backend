package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.ExperienceDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExperienceService {
    List<ExperienceDTO> listMyExperiences(UUID currentUserId);
    ExperienceDTO createMyExperience(UUID currentUserId, String companyName, String title,
                                     LocalDate startDate, LocalDate endDate, boolean isCurrent, String description);
    ExperienceDTO updateMyExperience(UUID currentUserId, UUID experienceId, String companyName, String title,
                                     LocalDate startDate, LocalDate endDate, Boolean isCurrent, String description);
    void deleteMyExperience(UUID currentUserId, UUID experienceId);
}
