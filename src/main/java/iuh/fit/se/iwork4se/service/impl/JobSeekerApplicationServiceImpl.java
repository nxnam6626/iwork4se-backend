package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.ApplyJobRequest;
import iuh.fit.se.iwork4se.dto.profile.MyApplicationDTO;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Application;
import iuh.fit.se.iwork4se.model.Job;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.repository.ApplicationRepository;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.JobSeekerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobSeekerApplicationServiceImpl implements JobSeekerApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository profileRepository;

    @Override
    public MyApplicationDTO applyToJob(UUID jobId, ApplyJobRequest request) {
        UUID userId = SecurityUtil.currentUserId();
        JobSeekerProfile profile = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));

        // Check if already applied
        if (applicationRepository.existsByJob_JobIdAndJobSeekerProfile_JobSeekerProfileId(jobId, profile.getJobSeekerProfileId())) {
            throw new IllegalArgumentException("Already applied to this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Application application = Application.builder()
                .job(job)
                .jobSeekerProfile(profile)
                .note(request.getNote())
                .build();

        application = applicationRepository.save(application);
        return toDTO(application);
    }

    @Override
    public Page<MyApplicationDTO> getMyApplications(int page, int size) {
        UUID userId = SecurityUtil.currentUserId();
        JobSeekerProfile profile = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));

        var pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        var pageApps = applicationRepository.findByJobSeekerProfile_JobSeekerProfileId(profile.getJobSeekerProfileId(), pageable);
        List<MyApplicationDTO> mapped = pageApps.getContent().stream().map(this::toDTO).toList();
        return new PageImpl<>(mapped, pageable, pageApps.getTotalElements());
    }

    @Override
    public MyApplicationDTO getMyApplication(UUID applicationId) {
        UUID userId = SecurityUtil.currentUserId();
        JobSeekerProfile profile = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Check ownership
        if (!application.getJobSeekerProfile().getJobSeekerProfileId().equals(profile.getJobSeekerProfileId())) {
            throw new ResourceNotFoundException("Application not found");
        }

        return toDTO(application);
    }

    private MyApplicationDTO toDTO(Application a) {
        return MyApplicationDTO.builder()
                .applicationId(a.getApplicationId())
                .jobId(a.getJob().getJobId())
                .jobTitle(a.getJob().getTitle())
                .companyName(a.getJob().getCompany().getName())
                .location(a.getJob().getLocation())
                .status(a.getStatus())
                .note(a.getNote())
                .appliedAt(a.getCreatedAt())
                .build();
    }
}
