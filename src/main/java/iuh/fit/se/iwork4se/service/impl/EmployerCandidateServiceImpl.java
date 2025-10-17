package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.CandidateDetailDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Education;
import iuh.fit.se.iwork4se.model.Experience;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.SeekerSkill;
import iuh.fit.se.iwork4se.repository.EducationRepository;
import iuh.fit.se.iwork4se.repository.ExperienceRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.SeekerSkillRepository;
import iuh.fit.se.iwork4se.service.EmployerCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployerCandidateServiceImpl implements EmployerCandidateService {

    private final JobSeekerProfileRepository profileRepository;
    private final SeekerSkillRepository seekerSkillRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;

    @Override
    public CandidateDetailDTO getCandidateDetail(UUID jobSeekerProfileId) {
        JobSeekerProfile p = profileRepository.findById(jobSeekerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<String> skills = seekerSkillRepository.findByJobSeekerProfile_JobSeekerProfileId(jobSeekerProfileId)
                .stream()
                .map(this::formatSkill)
                .toList();

        List<String> experiences = experienceRepository.findByJobSeekerProfile_JobSeekerProfileId(jobSeekerProfileId)
                .stream()
                .map(this::formatExperience)
                .toList();

        List<String> educations = educationRepository.findByJobSeekerProfile_JobSeekerProfileId(jobSeekerProfileId)
                .stream()
                .map(this::formatEducation)
                .toList();

        return CandidateDetailDTO.builder()
                .profileId(p.getJobSeekerProfileId())
                .fullName(p.getUser().getFullName())
                .headline(p.getHeadline())
                .summary(p.getSummary())
                .location(p.getLocation())
                .yearsExp(p.getYearsExp())
                .skills(skills)
                .experiences(experiences)
                .educations(educations)
                .build();
    }

    private String formatSkill(SeekerSkill ss) {
        return ss.getSkill().getName() + " - " + (ss.getLevel() == null ? "" : ss.getLevel());
    }

    private String formatExperience(Experience e) {
        String start = e.getStartDate() != null ? e.getStartDate().toString() : "?";
        String end = e.isCurrent() ? "Present" : (e.getEndDate() != null ? e.getEndDate().toString() : "?");
        return e.getTitle() + " @ " + e.getCompanyName() + " (" + start + " - " + end + ")";
    }

    private String formatEducation(Education ed) {
        String start = ed.getStartYear() != null ? ed.getStartYear().toString() : "?";
        String end = ed.getEndYear() != null ? ed.getEndYear().toString() : "?";
        return ed.getDegree() + " - " + ed.getMajor() + " @ " + ed.getSchool() + " (" + start + " - " + end + ")";
    }
}


