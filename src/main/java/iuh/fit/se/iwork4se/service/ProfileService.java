package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.JobSeekerProfileDTO;

import java.util.UUID;

public interface ProfileService {
    JobSeekerProfileDTO getMyProfile(UUID currentUserId);
    JobSeekerProfileDTO initMyProfileIfAbsent(UUID currentUserId);
    JobSeekerProfileDTO updateMyProfile(UUID currentUserId, String headline, String summary, String location, Integer yearsExp);
}
