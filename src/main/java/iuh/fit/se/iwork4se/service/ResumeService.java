package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.ResumeDTO;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    List<ResumeDTO> listMyResumes(UUID currentUserId);
    ResumeDTO createMyResume(UUID currentUserId, String title);
    ResumeDTO setMyCurrentFile(UUID currentUserId, UUID resumeId, UUID cvFileId);
    ResumeDTO renameMyResume(UUID currentUserId, UUID resumeId, String newTitle);
    void deleteMyResume(UUID currentUserId, UUID resumeId);
    ResumeDTO getMyResume(UUID currentUserId, UUID resumeId);
}
