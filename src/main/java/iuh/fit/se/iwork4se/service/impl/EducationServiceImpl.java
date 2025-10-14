package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.EducationDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Education;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.repository.EducationRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationServiceImpl implements EducationService {

    private final EducationRepository eduRepo;
    private final JobSeekerProfileRepository profileRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private void assertOwnedBy(UUID currentUserId, Education e) {
        if (!e.getJobSeekerProfile().getUser().getUserId().equals(currentUserId))
            throw new ForbiddenException("Not your education");
    }

    private EducationDTO toDTO(Education e) {
        return EducationDTO.builder()
                .id(e.getEducationId())
                .school(e.getSchool())
                .degree(e.getDegree())
                .major(e.getMajor())
                .startYear(e.getStartYear())
                .endYear(e.getEndYear())
                .description(e.getDescription())
                .jobSeekerProfileId(e.getJobSeekerProfile().getJobSeekerProfileId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationDTO> listMyEducations(UUID currentUserId) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        return eduRepo.findByJobSeekerProfile_JobSeekerProfileId(p.getJobSeekerProfileId()).stream().map(this::toDTO).toList();
    }

    @Override
    public EducationDTO createMyEducation(UUID currentUserId, String school, String degree, String major,
                                          LocalDate startYear, LocalDate endYear, String description) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        
        // Kiểm tra trùng lặp: cùng school + degree + major + startYear
        if (eduRepo.existsByJobSeekerProfile_JobSeekerProfileIdAndSchoolAndDegreeAndMajorAndStartYear(
                p.getJobSeekerProfileId(), school, degree, major, startYear)) {
            throw new IllegalArgumentException("Education record already exists with same school, degree, major and start year");
        }
        
        Education e = Education.builder()
                .jobSeekerProfile(p).school(school).degree(degree).major(major)
                .startYear(startYear).endYear(endYear).description(description)
                .build();
        return toDTO(eduRepo.save(e));
    }

    @Override
    public EducationDTO updateMyEducation(UUID currentUserId, UUID educationId, String school, String degree, String major,
                                          LocalDate startYear, LocalDate endYear, String description) {
        Education e = eduRepo.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));
        assertOwnedBy(currentUserId, e);
        if (school != null) e.setSchool(school);
        if (degree != null) e.setDegree(degree);
        if (major != null) e.setMajor(major);
        if (startYear != null) e.setStartYear(startYear);
        if (endYear != null) e.setEndYear(endYear);
        if (description != null) e.setDescription(description);
        return toDTO(eduRepo.save(e));
    }

    @Override
    public void deleteMyEducation(UUID currentUserId, UUID educationId) {
        Education e = eduRepo.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));
        assertOwnedBy(currentUserId, e);
        eduRepo.delete(e);
    }
}
