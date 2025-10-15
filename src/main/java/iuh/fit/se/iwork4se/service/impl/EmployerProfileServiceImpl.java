package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.EmployerProfileDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateEmployerProfileRequest;
import iuh.fit.se.iwork4se.model.Company;
import iuh.fit.se.iwork4se.model.EmployerProfile;
import iuh.fit.se.iwork4se.model.Role;
import iuh.fit.se.iwork4se.model.User;
import iuh.fit.se.iwork4se.repository.CompanyRepository;
import iuh.fit.se.iwork4se.repository.EmployerProfileRepository;
import iuh.fit.se.iwork4se.repository.UserRepository;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.EmployerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployerProfileServiceImpl implements EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private static EmployerProfileDTO toDto(EmployerProfile ep) {
        return EmployerProfileDTO.builder()
                .employerProfileId(ep.getEmployerProfileId())
                .userId(ep.getUser().getUserId())
                .companyId(ep.getCompany() != null ? ep.getCompany().getCompanyId() : null)
                .companyName(ep.getCompany() != null ? ep.getCompany().getName() : null)
                .title(ep.getTitle())
                .department(ep.getDepartment())
                .contactEmail(ep.getContactEmail())
                .contactPhone(ep.getContactPhone())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public EmployerProfileDTO getMyEmployerProfile() {
        UUID userId = SecurityUtil.currentUserId();
        EmployerProfile ep = employerProfileRepository.findByUser_UserId(userId)
                .orElseGet(() -> autoCreateForUser(userId));
        return toDto(ep);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYER')")
    public EmployerProfileDTO upsertMyEmployerProfile(UpdateEmployerProfileRequest request) {
        UUID userId = SecurityUtil.currentUserId();
        EmployerProfile ep = employerProfileRepository.findByUser_UserId(userId)
                .orElseGet(() -> autoCreateForUser(userId));

        Company company = ep.getCompany();
        if (company == null) company = new Company();

        if (request.getCompanyName() != null) company.setName(request.getCompanyName());
        if (request.getIndustry() != null) company.setIndustry(request.getIndustry());
        if (request.getDescription() != null) company.setDescription(request.getDescription());
        if (request.getHeadquarters() != null) company.setHeadquarters(request.getHeadquarters());
        if (request.getSize() != null) company.setSize(request.getSize());
        if (request.getFoundedYear() != null) company.setFoundedYear(request.getFoundedYear());

        company = companyRepository.save(company);
        ep.setCompany(company);

        if (request.getTitle() != null) ep.setTitle(request.getTitle());
        if (request.getDepartment() != null) ep.setDepartment(request.getDepartment());
        if (request.getContactEmail() != null) ep.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) ep.setContactPhone(request.getContactPhone());

        ep = employerProfileRepository.save(ep);
        return toDto(ep);
    }

    private EmployerProfile autoCreateForUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        // Only allow create if user is EMPLOYER
        if (user.getRole() != Role.EMPLOYER) {
            throw new org.springframework.security.access.AccessDeniedException("Not employer");
        }
        EmployerProfile created = EmployerProfile.builder()
                .user(user)
                .build();
        return employerProfileRepository.save(created);
    }
}


