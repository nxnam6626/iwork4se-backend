package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.CVFileDTO;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.CVFile;
import iuh.fit.se.iwork4se.model.Resume;
import iuh.fit.se.iwork4se.repository.CVFileRepository;
import iuh.fit.se.iwork4se.repository.ResumeRepository;
import iuh.fit.se.iwork4se.service.CVFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CVFileServiceImpl implements CVFileService {

    private final CVFileRepository cvRepo;
    private final ResumeRepository resumeRepo;

    private CVFileDTO toDTO(CVFile f) {
        return CVFileDTO.builder()
                .id(f.getCVId())
                .ownerUserId(f.getOwnerUserId())
                .objectKey(f.getObjectKey())
                .fileName(f.getFileName())
                .mimeType(f.getMimeType())
                .size(f.getSize())
                .uploadedAt(f.getUploadedAt())
                .resumeId(f.getResume() != null ? f.getResume().getResumeId() : null)
                .build();
    }

    @Override
    public CVFileDTO createMyCVFile(UUID currentUserId, UUID resumeId, String objectKey, String fileName, String mimeType, long size) {
        Resume r = null;
        if (resumeId != null) {
            r = resumeRepo.findById(resumeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
            // Ownership: resume phải thuộc currentUser
            if (!r.getJobSeekerProfile().getUser().getUserId().equals(currentUserId))
                throw new ForbiddenException("Not your resume");
        }
        CVFile f = CVFile.builder()
                .ownerUserId(currentUserId)
                .resume(r)
                .objectKey(objectKey)
                .fileName(fileName)
                .mimeType(mimeType)
                .size(size)
                .uploadedAt(Instant.now())
                .build();
        return toDTO(cvRepo.save(f));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CVFileDTO> listMyCVFiles(UUID currentUserId) {
        return cvRepo.findByOwnerUserId(currentUserId).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CVFileDTO getMyCVFile(UUID currentUserId, UUID cvFileId) {
        CVFile f = cvRepo.findById(cvFileId)
                .orElseThrow(() -> new ResourceNotFoundException("CVFile not found"));
        if (!f.getOwnerUserId().equals(currentUserId)) throw new ForbiddenException("Not your CV file");
        return toDTO(f);
    }
}
