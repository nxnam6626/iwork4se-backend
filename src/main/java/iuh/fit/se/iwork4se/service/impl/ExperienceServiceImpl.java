package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.ExperienceDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Experience;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.repository.ExperienceRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository expRepo;
    private final JobSeekerProfileRepository profileRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private void assertOwnedBy(UUID currentUserId, Experience e) {
        UUID owner = e.getJobSeekerProfile().getUser().getUserId();
        if (!owner.equals(currentUserId)) throw new ForbiddenException("Not your experience");
    }

    private ExperienceDTO toDTO(Experience e) {
        return ExperienceDTO.builder()
                .id(e.getExperienceId())
                .companyName(e.getCompanyName())
                .title(e.getTitle())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .isCurrent(e.isCurrent())
                .description(e.getDescription())
                .profileId(e.getJobSeekerProfile().getJobSeekerProfileId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDTO> listMyExperiences(UUID currentUserId) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        return expRepo.findByJobSeekerProfile_JobSeekerProfileId(p.getJobSeekerProfileId()).stream().map(this::toDTO).toList();
    }

    @Override
    public ExperienceDTO createMyExperience(UUID currentUserId, String companyName, String title,
                                            LocalDate startDate, LocalDate endDate, boolean isCurrent, String description) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        Experience e = Experience.builder()
                .jobSeekerProfile(p).companyName(companyName).title(title)
                .startDate(startDate).endDate(endDate).isCurrent(isCurrent).description(description)
                .build();
        return toDTO(expRepo.save(e));
    }

    @Override
    public ExperienceDTO updateMyExperience(UUID currentUserId, UUID experienceId, String companyName, String title,
                                            LocalDate startDate, LocalDate endDate, Boolean isCurrent, String description) {
        Experience e = expRepo.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        assertOwnedBy(currentUserId, e);
        if (companyName != null) e.setCompanyName(companyName);
        if (title != null) e.setTitle(title);
        if (startDate != null) e.setStartDate(startDate);
        if (endDate != null) e.setEndDate(endDate);
        if (isCurrent != null) e.setCurrent(isCurrent);
        if (description != null) e.setDescription(description);
        return toDTO(expRepo.save(e));
    }

    @Override
    public void deleteMyExperience(UUID currentUserId, UUID experienceId) {
        Experience e = expRepo.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        assertOwnedBy(currentUserId, e);
        expRepo.delete(e);
    }
}
