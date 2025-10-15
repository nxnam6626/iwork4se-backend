package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.EmployerProfileDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateEmployerProfileRequest;

public interface EmployerProfileService {
    EmployerProfileDTO getMyEmployerProfile();
    EmployerProfileDTO upsertMyEmployerProfile(UpdateEmployerProfileRequest request);
}


