package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.ApplicantSummaryDTO;
import iuh.fit.se.iwork4se.dto.profile.ApplicationDetailDTO;
import iuh.fit.se.iwork4se.dto.profile.UpdateApplicationStatusRequest;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.*;
import iuh.fit.se.iwork4se.repository.*;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.EmployerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployerApplicationServiceImpl implements EmployerApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final EmployerProfileRepository employerProfileRepository;

    @Override
    public Page<ApplicantSummaryDTO> listApplicants(Integer page, Integer size, ApplicationStatus status, UUID jobId) {
        Company company = getMyCompanyOrThrow();
        var pageable = PageRequest.of(Math.max(page == null ? 0 : page, 0), Math.max(size == null ? 10 : size, 1));
        Page<Application> result;
        if (jobId != null) {
            result = applicationRepository.findByJob_JobId(jobId, pageable);
        } else if (status != null) {
            result = applicationRepository.findByJob_Company_CompanyIdAndStatus(company.getCompanyId(), status, pageable);
        } else {
            result = applicationRepository.findByJob_Company_CompanyId(company.getCompanyId(), pageable);
        }
        List<ApplicantSummaryDTO> mapped = result.getContent().stream().map(this::toSummary).toList();
        return new PageImpl<>(mapped, pageable, result.getTotalElements());
    }

    @Override
    public ApplicationDetailDTO getApplication(UUID applicationId) {
        Application app = getOwnedApplication(applicationId);
        return toDetail(app);
    }

    @Override
    public ApplicationDetailDTO updateStatus(UUID applicationId, UpdateApplicationStatusRequest request) {
        Application app = getOwnedApplication(applicationId);
        ApplicationStatus from = app.getStatus();
        app.setStatus(request.getToStatus());
        if (request.getNote() != null) app.setNote(request.getNote());
        app = applicationRepository.save(app);

        ApplicationStatusHistory h = ApplicationStatusHistory.builder()
                .application(app)
                .fromStatus(from)
                .toStatus(request.getToStatus())
                .note(request.getNote())
                .build();
        historyRepository.save(h);

        return toDetail(app);
    }

    private Company getMyCompanyOrThrow() {
        UUID userId = SecurityUtil.currentUserId();
        EmployerProfile ep = employerProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ForbiddenException("Employer profile not found"));
        Company company = ep.getCompany();
        if (company == null) throw new ForbiddenException("Company profile is missing");
        return company;
    }

    private Application getOwnedApplication(UUID applicationId) {
        Company company = getMyCompanyOrThrow();
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!app.getJob().getCompany().getCompanyId().equals(company.getCompanyId())) {
            throw new ForbiddenException("Not owner of this application");
        }
        return app;
    }

    private ApplicantSummaryDTO toSummary(Application a) {
        JobSeekerProfile p = a.getJobSeekerProfile();
        return ApplicantSummaryDTO.builder()
                .applicationId(a.getApplicationId())
                .jobId(a.getJob().getJobId())
                .jobTitle(a.getJob().getTitle())
                .jobSeekerProfileId(p.getJobSeekerProfileId())
                .candidateName(p.getUser().getFullName())
                .candidateHeadline(p.getHeadline())
                .candidateLocation(p.getLocation())
                .status(a.getStatus())
                .build();
    }

    private ApplicationDetailDTO toDetail(Application a) {
        JobSeekerProfile p = a.getJobSeekerProfile();
        return ApplicationDetailDTO.builder()
                .applicationId(a.getApplicationId())
                .jobId(a.getJob().getJobId())
                .jobTitle(a.getJob().getTitle())
                .jobSeekerProfileId(p.getJobSeekerProfileId())
                .candidateName(p.getUser().getFullName())
                .candidateHeadline(p.getHeadline())
                .candidateLocation(p.getLocation())
                .status(a.getStatus())
                .note(a.getNote())
                .createdAt(a.getCreatedAt())
                .build();
    }
}


