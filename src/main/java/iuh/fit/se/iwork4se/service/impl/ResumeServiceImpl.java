package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.ResumeDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.CVFile;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.model.Resume;
import iuh.fit.se.iwork4se.repository.CVFileRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.repository.ResumeRepository;
import iuh.fit.se.iwork4se.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepo;
    private final JobSeekerProfileRepository profileRepo;
    private final CVFileRepository cvRepo;

    private JobSeekerProfile requireMyProfile(UUID currentUserId) {
        return profileRepo.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    private void assertOwnedBy(UUID currentUserId, Resume r) {
        if (!r.getJobSeekerProfile().getUser().getUserId().equals(currentUserId))
            throw new ForbiddenException("Not your resume");
    }

    private ResumeDTO toDTO(Resume r) {
        return ResumeDTO.builder()
                .id(r.getResumeId())
                .title(r.getTitle())
                .updatedAt(r.getUpdatedAt())
                .profileId(r.getJobSeekerProfile().getJobSeekerProfileId())
                .currentFileId(r.getCurrentFile() != null ? r.getCurrentFile().getCVId() : null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDTO> listMyResumes(UUID currentUserId) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        return resumeRepo.findByJobSeekerProfile_JobSeekerProfileId(p.getJobSeekerProfileId()).stream().map(this::toDTO).toList();
    }

    @Override
    public ResumeDTO createMyResume(UUID currentUserId, String title) {
        JobSeekerProfile p = requireMyProfile(currentUserId);
        Resume r = Resume.builder().jobSeekerProfile(p).title(title).updatedAt(Instant.now()).build();
        return toDTO(resumeRepo.save(r));
    }

    @Override
    public ResumeDTO setMyCurrentFile(UUID currentUserId, UUID resumeId, UUID cvFileId) {
        Resume r = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        assertOwnedBy(currentUserId, r);
        CVFile f = cvRepo.findById(cvFileId)
                .orElseThrow(() -> new ResourceNotFoundException("CVFile not found"));
        if (!f.getOwnerUserId().equals(currentUserId)) throw new ForbiddenException("Not your CV file");
        r.setCurrentFile(f);
        r.setUpdatedAt(Instant.now());
        return toDTO(resumeRepo.save(r));
    }

    @Override
    public ResumeDTO renameMyResume(UUID currentUserId, UUID resumeId, String newTitle) {
        Resume r = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        assertOwnedBy(currentUserId, r);
        r.setTitle(newTitle);
        r.setUpdatedAt(Instant.now());
        return toDTO(resumeRepo.save(r));
    }

    @Override
    public void deleteMyResume(UUID currentUserId, UUID resumeId) {
        Resume r = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        assertOwnedBy(currentUserId, r);
        resumeRepo.delete(r);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDTO getMyResume(UUID currentUserId, UUID resumeId) {
        Resume r = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        assertOwnedBy(currentUserId, r);
        return toDTO(r);
    }
}
