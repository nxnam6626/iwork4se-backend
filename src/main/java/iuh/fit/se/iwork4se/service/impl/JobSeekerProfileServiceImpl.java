package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.JobSeekerProfileDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.UserRepository;
import iuh.fit.se.iwork4se.service.JobSeekerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JobSeekerProfileServiceImpl implements JobSeekerProfileService {

    private final JobSeekerProfileRepository profileRepo;
    private final UserRepository userRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for current user"));
    }

    private JobSeekerProfileDTO toDTO(JobSeekerProfile p) {
        return JobSeekerProfileDTO.builder()
                .id(p.getJobSeekerProfileId())
                .headline(p.getHeadline())
                .summary(p.getSummary())
                .location(p.getLocation())
                .yearsExp(p.getYearsExp())
                .userId(p.getUser() != null ? p.getUser().getUserId() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public JobSeekerProfileDTO getMyProfile(UUID currentUserId) {
        return toDTO(requireMyProfile(currentUserId));
    }

    @Override
    public JobSeekerProfileDTO initMyProfileIfAbsent(UUID currentUserId) {
        JobSeekerProfile p = profileRepo.findByUser_UserId(currentUserId).orElseGet(() -> {
            User u = userRepo.findById(currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return profileRepo.save(JobSeekerProfile.builder()
                    .user(u).yearsExp(0).build());
        });
        return toDTO(p);
    }

    @Override
    public JobSeekerProfileDTO updateMyProfile(UUID currentUserId, String headline, String summary, String location, Integer yearsExp) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        if (headline != null) p.setHeadline(headline);
        if (summary != null) p.setSummary(summary);
        if (location != null) p.setLocation(location);
        if (yearsExp != null && yearsExp >= 0) p.setYearsExp(yearsExp);
        return toDTO(profileRepo.save(p));
    }
}
